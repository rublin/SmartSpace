package org.rublin.service;

import org.rublin.controller.NotificationService;
import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.model.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.rublin.repository.EventRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Sheremet on 16.06.2016.
 */
@Service
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
            if (event.isDigital() && trigger.getName().equals("Move 1 floor 2") && !(boolean)event.getState()) {
                LocalDateTime now = LocalDateTime.now();
                if (now.getHour() >= 5 &&
                        now.getHour() < 6 &&
                        now.getMinute() >= 30 &&
                        now.getDayOfWeek() != DayOfWeek.SATURDAY &&
                        now.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            notificationService.sayWeather();
                        }
                    }).start();
                }

            }
        } else if (trigger.getType() == Type.DIGITAL && !trigger.isSecure()) {
            alarmEvent(event, trigger, zone);
        } else if (trigger.getType() == Type.ANALOG && trigger.getMinThreshold() > (double)event.getState() || trigger.getMaxThreshold() < (double)event.getState()) {
            alarmEvent(event, trigger, zone);
        } else {
            eventRepository.save(trigger, event);
        }
    }

    @Override
    public List<Event> get(Trigger trigger) {
        return eventRepository.get(trigger);
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
        event.setAlarm(true);
        eventRepository.save(trigger, event);
        zoneService.setStatus(zone, ZoneStatus.RED);
        zoneService.sendNotification(zone, trigger.isSecure());
    }

    private void setTriggerState (Event event, Trigger trigger, Zone zone) {
        if (event.isDigital()) {
            trigger.setState((Boolean) event.getState());
        } else if (trigger.getMinThreshold() > (double)event.getState() || trigger.getMaxThreshold() < (double)event.getState()){
            trigger.setState(false);
        } else {
            trigger.setState(true);
        }
        triggerService.save(trigger, zone);
    }
}
