package org.rublin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.rublin.controller.NotificationService;
import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.model.event.Event;
import org.rublin.repository.EventRepository;
import org.rublin.service.EventService;
import org.rublin.service.TriggerService;
import org.rublin.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sheremet on 16.06.2016.
 */
@Service
@Slf4j
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ZoneService zoneService;
    @Autowired
    private TriggerService triggerService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public void save(Trigger trigger, Event event) {
        if (trigger.isActive()) {
            Zone zone = trigger.getZone();
            setTriggerState(event, trigger, zone);
            if (trigger.isSecure()) {
                if (zone.isSecure()) {
                    if (trigger.getType() == Type.DIGITAL || trigger.getMinThreshold() > (double) event.getState() || trigger.getMaxThreshold() < (double) event.getState()) {
                        alarmEvent(event, trigger, zone);
                    } else {
                        eventRepository.save(trigger, event);
                    }
                } else {
                    eventRepository.save(trigger, event);
                }
            } else if (trigger.getType() == Type.DIGITAL && !trigger.isSecure()) {
                if ((boolean) event.getState()) {
                    eventRepository.save(trigger, event);
                } else {
                    alarmEvent(event, trigger, zone);
                }
            } else if (trigger.getType() == Type.ANALOG && trigger.getMinThreshold() > (double) event.getState() || trigger.getMaxThreshold() < (double) event.getState()) {
                alarmEvent(event, trigger, zone);
            } else {
                eventRepository.save(trigger, event);
            }
        } else {
            log.warn("Trigger {} is disabled", trigger.getName());
        }
    }

    @Override
    public List<Event> get(Trigger trigger) {
        return eventRepository.get(trigger);
    }

    @Override
    public List<Event> get(Trigger trigger, int numberLatestEvents) {
        List<Event> events = eventRepository.get(trigger);
        return events.stream()
                .limit(numberLatestEvents)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.getAll();
    }

    @Override
    public List<Event> getBetween(LocalDateTime from, LocalDateTime to) {
        return eventRepository.getBetween(from, to);
    }

    @Override
    public List<Event> getAlarmed() {
        return eventRepository.getAlarmed();
    }

    private void alarmEvent(Event event, Trigger trigger, Zone zone) {
        LocalDateTime now = LocalDateTime.now();
        long securedZones = zoneService.getAll().stream()
                .filter(Zone::isSecure)
                .count();
        List<Event> last5MinEvents = getBetween(now.minusMinutes(5), now);
        boolean morningDetectorZoneActive = last5MinEvents.stream()
                .anyMatch(e -> e.getTrigger().isMorningDetector());

        if (zone.isNightSecurity() && securedZones == 1 && morningDetectorZoneActive) {
            zone.setSecure(false);
            return;
        }
        LocalDateTime changed = zone.getSecurityChanged();
        // Event is after 2 minutes, because arduino delayed movement events (no moving) for 1 minute
        if (ChronoUnit.MINUTES.between(changed, LocalDateTime.now()) > 2) {
            event.setAlarm(true);
            eventRepository.save(trigger, event);
            zoneService.setStatus(zone, ZoneStatus.RED);
            zoneService.sendNotification(zone, trigger.isSecure());
        }
    }

    private void setTriggerState(Event event, Trigger trigger, Zone zone) {
        if (event.isDigital()) {
            trigger.setState((Boolean) event.getState());
        } else if (trigger.getMinThreshold() > (double) event.getState() || trigger.getMaxThreshold() < (double) event.getState()) {
            trigger.setState(false);
        } else {
            trigger.setState(true);
        }
        triggerService.save(trigger, zone);
    }
}
