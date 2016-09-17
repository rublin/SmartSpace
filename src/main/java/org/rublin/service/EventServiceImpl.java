package org.rublin.service;

import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.model.event.Event;
import org.rublin.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.rublin.repository.EventRepository;

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

    @Override
    public void save(Trigger trigger, Event event) {
        Zone zone = trigger.getZone();
        if (trigger.isSecure()) {
            if (zone.isSecure()) {
                if (trigger.getType() == Type.DIGITAL || trigger.getMinThreshold() > (double)event.getState() || trigger.getMaxThreshold() < (double)event.getState()){
                    event.setAlarm(true);
                    eventRepository.save(trigger, event);
                    if (zone.getStatus() != ZoneStatus.RED) {
                        zoneService.setStatus(zone, ZoneStatus.RED);
                    } else {
                        zoneService.sendNotification(zone);
                    }
                }
            }
        } else if (trigger.getType() == Type.ANALOG && trigger.getMinThreshold() > (double)event.getState() || trigger.getMaxThreshold() < (double)event.getState()) {
            event.setAlarm(true);
            eventRepository.save(trigger, event);
            zoneService.setStatus(zone, ZoneStatus.RED);
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
}
