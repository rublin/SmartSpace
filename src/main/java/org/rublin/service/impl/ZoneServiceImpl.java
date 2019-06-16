package org.rublin.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.controller.NotificationService;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.model.event.Event;
import org.rublin.repository.ZoneRepositoryJpa;
import org.rublin.service.EventService;
import org.rublin.service.ZoneService;
import org.rublin.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepositoryJpa zoneRepository;

    private final EventService eventService;

    private final NotificationService notificationService;

    @Value("${zone.activity.threshold.minutes}")
    private Integer threshold;

    @Value("${zone.night.period.workdays}")
    private String[] nightPeriodWorkdays;

    @Value("${zone.night.period.weekends}")
    private String[] nightPeriodWeekends;

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
            zone.setSecurityChanged(now());
            if (!security) {
                zone.setStatus(ZoneStatus.GREEN);
            }
            save(zone);
            log.info("change Zone secure state to {}", zone.isSecure());
            notificationService.notifyAdmin(zone + " security state is changed to " + security);
        } else {
            log.info("Zone {} already {}", zone.getName(), security ? "armed" : "disarmed");
        }
    }

    @Override
    public Zone setStatus(Zone zone, ZoneStatus status) {
        zone.setStatus(status);
        save(zone);
        return zone;
    }

    @Override
    public synchronized void activity() {
        LocalDateTime now = now();
        List<Event> lastEvents = eventService.getBetween(now.minusMinutes(threshold), now);
        for (Zone zone : getAll()) {
            long amountOfEventByZone = lastEvents.stream()
                    .filter(event -> event.getTrigger().getZone().equals(zone))
                    .filter(event -> event.getTrigger().isSecure())
                    .count();
            boolean active = checkZoneActivity((int) amountOfEventByZone);
            log.debug("Zone {} has {} events by last hour. Activity is {}", zone.getName(), amountOfEventByZone, active);
            if (zone.isActive() != active) {
                log.info("Zone {} set activity to {}", zone.getName(), active);
                zone.setActive(active);
                save(zone);

                if (zone.isNightSecurity() &&
                        !zone.isSecure() &&
                        nightTime(now())) {
                    log.info("It's time to automatically enable secure for zone {}", zone);
                    setSecure(zone, true);
                }

                if (active && zone.isMorningDetector() &&
                        LocalDate.now()
                                .isAfter(zone.getLastMorningNotification().toLocalDate())) {
                    zone.setLastMorningNotification(LocalDateTime.now());
                    save(zone);
                    notificationService.morningNotifications();
                    getAll().stream()
                            .filter(Zone::isSecure)
                            .filter(Zone::isNightSecurity)
                            .forEach(z -> setSecure(z, false));
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

    boolean checkZoneActivity(int events) {
        if (nightTime(now())) {
            return events > 5;
        }
        return events >= 1;
    }

    boolean morningStarts(LocalDateTime now) {
        LocalTime workdaysMorningStart = LocalTime.parse(nightPeriodWorkdays[1]);
        LocalTime weekdaysMorningStart = LocalTime.parse(nightPeriodWeekends[1]);

        if (isWeekend(now)) {
            return now.toLocalTime().isAfter(weekdaysMorningStart) && now.toLocalTime().isBefore(weekdaysMorningStart.plusHours(2));
        } else {
            return now.toLocalTime().isAfter(workdaysMorningStart) && now.toLocalTime().isBefore(workdaysMorningStart.plusHours(2));
        }
    }

    boolean nightTime(LocalDateTime now) {
        if (isWeekend(now)) {
            return now.toLocalTime().isAfter(LocalTime.parse(nightPeriodWeekends[0])) || now.toLocalTime().isBefore(LocalTime.parse(nightPeriodWeekends[1]));
        } else {
            return now.toLocalTime().isAfter(LocalTime.parse(nightPeriodWorkdays[0])) || now.toLocalTime().isBefore(LocalTime.parse(nightPeriodWorkdays[1]));
        }

    }

    boolean isWeekend(LocalDateTime now) {
        return DayOfWeek.SUNDAY == now.getDayOfWeek() || DayOfWeek.SATURDAY == now.getDayOfWeek();
    }
}
