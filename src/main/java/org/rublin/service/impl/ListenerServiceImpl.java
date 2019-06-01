package org.rublin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.events.OnHeatingEvent;
import org.rublin.events.OnHeatingStopEvent;
import org.rublin.events.OnNewEvent;
import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.model.event.Event;
import org.rublin.service.EventService;
import org.rublin.service.TriggerService;
import org.rublin.service.ZoneService;
import org.rublin.service.delayed.DelayQueueService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListenerServiceImpl {

    private final DelayQueueService delayQueueService;
    private final ZoneService zoneService;
    private final EventService eventService;
    private final TriggerService triggerService;

    @Async
    @EventListener
    public void heatingListener(OnHeatingEvent event) {
        log.info("Received heating {} event with delay {} sec", event.isPumpStatus(), event.getStartTime());
        delayQueueService.put(event);
    }

    @Async
    @EventListener
    public void heatingStopListener(OnHeatingStopEvent event) {
        log.info("Received heating stop event. All the delayed processes will be deleted");
        delayQueueService.clearHeating();
    }

    @Async
    @EventListener
    public void newEventListener(OnNewEvent onNewEvent) {
        Event event = onNewEvent.getEvent();
        Trigger trigger = event.getTrigger();
        if (trigger.isActive()) {
            Zone zone = trigger.getZone();
            setTriggerState(event, trigger, zone);
            if (trigger.isSecure()) {
                if (zone.isSecure()) {
                    if (trigger.getType() == Type.DIGITAL || trigger.getMinThreshold() > (double) event.getState() || trigger.getMaxThreshold() < (double) event.getState()) {
                        processAlarmEvent(event);
                    } else {
                        eventService.save(event);
                    }
                } else {
                    eventService.save(event);
                }
            } else if (trigger.getType() == Type.DIGITAL && !trigger.isSecure()) {
                if ((boolean) event.getState()) {
                    eventService.save(event);
                } else {
                    processAlarmEvent(event);
                }
            } else if (trigger.getType() == Type.ANALOG && trigger.getMinThreshold() > (double) event.getState() || trigger.getMaxThreshold() < (double) event.getState()) {
                processAlarmEvent(event);
            } else {
                eventService.save(event);
            }
        } else {
            log.warn("Trigger {} is disabled", trigger.getName());
        }
    }

    private void processAlarmEvent(Event event) {
        Trigger trigger = event.getTrigger();
        Zone zone = trigger.getZone();
        LocalDateTime now = LocalDateTime.now();
        long securedZones = zoneService.getAll().stream()
                .filter(Zone::isSecure)
                .count();
        List<Event> last5MinEvents = eventService.getBetween(now.minusMinutes(5), now);
        boolean morningDetectorZoneActive = last5MinEvents.stream()
                .anyMatch(e -> e.getTrigger().isMorningDetector());

        if (zone.isNightSecurity() && securedZones == 1 && morningDetectorZoneActive) {
            zoneService.setSecure(zone, false);
            eventService.save(event);
            return;
        }
        LocalDateTime changed = zone.getSecurityChanged();
        // Event is after 2 minutes, because arduino delayed movement events (no moving) for 1 minute
        if (ChronoUnit.MINUTES.between(changed, LocalDateTime.now()) > 2) {
            event.setAlarm(true);
            eventService.save(event);
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
