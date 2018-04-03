package org.rublin.service;

import org.rublin.controller.NotificationService;
import org.rublin.model.Trigger;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.model.event.Event;
import org.rublin.repository.ZoneRepository;
import org.rublin.util.exception.ExceptionUtil;
import org.rublin.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 11.07.2016.
 */
@Service
public class ZoneServiceImpl implements ZoneService {

    private static final Logger LOG = getLogger(ZoneServiceImpl.class);

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Zone save(Zone zone) {
        return zoneRepository.save(zone);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(zoneRepository.delete(id), id);
    }

    @Override
    public Zone get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(zoneRepository.get(id), id);
    }

    @Override
    public Collection<Zone> getAll() {
        return zoneRepository.getAll();
    }

    @Override
    public void setSecure(Zone zone, boolean security) {
        zone.setSecure(security);
        if (!security) {
            zone.setStatus(ZoneStatus.GREEN);
        }
        zoneRepository.save(zone);
        LOG.info("change Zone secure state to {}", zone.isSecure());
        notificationService.sendInfoToAllUsers(zone);
    }

    @Override
    public Zone setStatus(Zone zone, ZoneStatus status) {
        zone.setStatus(status);
        zoneRepository.save(zone);
        return zone;
    }

    @Override
    public String getInfo(Zone zone) {
        return String.format(
                "id: <b>%d</b>, name: <b>%s</b>, status: <b>%s</b>, secure: <b>%s</b>",
                zone.getId(),
                zone.getName(),
                zone.getStatus().toString(),
                zone.isSecure() ? "YES" : "NO");    }

    @Override
    public void activity() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> lastHourEvents = eventService.getBetween(now.minusHours(1), now);
        Map<Trigger, List<Event>> eventsByTrigger = lastHourEvents.stream()
                .collect(groupingBy(Event::getTrigger));
        for (Zone zone : getAll()) {
            boolean active = zone.getTriggers().stream()
                    .filter(Trigger::isSecure)
                    .anyMatch(trigger -> Objects.nonNull(eventsByTrigger.get(trigger)));
            LOG.debug("Zone {} activity is {}", zone.getName(), active);
            if (zone.isActive() != active) {
                LOG.info("Zone {} set activity to {}", zone.getName(), active);
                zone.setActive(active);
                zoneRepository.save(zone);
            }
        }
    }

    @Override
    public void sendNotification(Zone zone, boolean isSecure) {
        LOG.info("Notification sending");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                notificationService.sendAlarmNotification(zone, isSecure);
            }
        });
        thread.start();
    }
}
