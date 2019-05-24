package org.rublin.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.rublin.controller.NotificationService;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.model.event.Event;
import org.rublin.repository.ZoneRepositoryJpa;
import org.rublin.service.EventService;
import org.rublin.service.ZoneService;
import org.rublin.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

/**
 * Created by Sheremet on 11.07.2016.
 */
@Slf4j
@Service
public class ZoneServiceImpl implements ZoneService {
    
    @Autowired
    private ZoneRepositoryJpa zoneRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private NotificationService notificationService;

    @Value("${zone.activity.threshold.minutes}")
    private Integer threshold;

    @Value("${zone.night.period}")
    private String[] nightPeriod;

    @Override
    public Zone save(Zone zone) {
        return zoneRepository.save(zone);
    }

    @Override
    public void delete(int id) {
        zoneRepository.deleteById(id);
    }

    @Override
    public Zone get(int id) throws NotFoundException {
        return zoneRepository.findById(id).orElseThrow(() -> new NotFoundException("Zone with id=" + id + " not found"));
    }

    @Override
    public Collection<Zone> getAll() {
        return Lists.newArrayList(zoneRepository.findAll());
    }

    @Override
    public void setSecure(Zone zone, boolean security) {
        if (zone.isSecure() != security) {
            zone.setSecure(security);
            zone.setSecurityChanged(LocalDateTime.now());
            if (!security) {
                zone.setStatus(ZoneStatus.GREEN);
            }
            zoneRepository.save(zone);
            log.info("change Zone secure state to {}", zone.isSecure());
            notificationService.notifyAdmin(zone + " security state is changed to " + security);
        } else {
            log.info("Zone {} already {}", zone.getName(), security ? "armed" : "disarmed");
        }
    }

    @Override
    public Zone setStatus(Zone zone, ZoneStatus status) {
        zone.setStatus(status);
        zoneRepository.save(zone);
        return zone;
    }

    @Override
    public  void activity() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> lastEvents = eventService.getBetween(now.minusMinutes(threshold), now);
        for (Zone zone : getAll()) {
            long amountOfEventByZone = lastEvents.stream()
                    .filter(event -> event.getTrigger().getZone().equals(zone))
                    .count();
            boolean active = checkZoneActivity((int) amountOfEventByZone);
            log.debug("Zone {} has {} events by last hour. Activity is {}", zone.getName(), amountOfEventByZone, active);
            if (zone.isActive() != active) {
                log.info("Zone {} set activity to {}", zone.getName(), active);
                zone.setActive(active);
                zoneRepository.save(zone);

                if (zone.isNightSecurity() &&
                        !zone.isSecure() &&
                        (now.toLocalTime().isAfter(LocalTime.parse(nightPeriod[0])) || now.toLocalTime().isBefore(LocalTime.parse(nightPeriod[1])))) {
                    log.info("It's time to automatically enable secure for zone {}", zone);
                    setSecure(zone, true);
                    notificationService.notifyAdmin(zone + " automatically armed");
                }
            }
        }
    }

    @Override
    public void sendNotification(Zone zone, boolean isSecure) {
        log.info("Notification sending");
        Thread thread = new Thread(() -> notificationService.sendAlarmNotification(zone, isSecure));
        thread.start();
    }

    protected boolean checkZoneActivity(int events) {
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() < 5) {
            return events > 5;
        } else if (now.getHour() == 5 && now.getMinute() < 30) {
            return events > 5;
        } else {
            return events >= 1;
        }
    }
}
