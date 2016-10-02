package org.rublin.web;

import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.rublin.service.EventService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 02.10.2016.
 */
public class AbstractEventController {
    protected final Logger LOG = getLogger(getClass());

    @Autowired
    private EventService eventService;

    public List<Event> getAll() {
        LOG.debug("get all events");
        return (List<Event>) eventService.getAll();
    }

    public List<Event> getByTrigger(Trigger trigger) {
        LOG.info("select events by trigger {}", trigger);
        return (List<Event>)eventService.get(trigger);
    }

    public void create(String state, Trigger trigger) {
        Event event;
        if (trigger.getType() == Type.ANALOG) {
            event = new AnalogEvent(trigger, Double.valueOf(state));
        } else {
            event = new DigitEvent(trigger, Boolean.valueOf(state));
        }
        eventService.save(trigger, event);
        LOG.info("new event {} added", event);
    }

    public List<Event> getBetween(LocalDateTime from, LocalDateTime to) {
        LOG.debug("get between {} and {}", from, to);
        return eventService.getBetween(from, to);
    }

    public List<Event> getAlarmed() {
        LOG.debug("get alarmed");
        return eventService.getAlarmed();
    }
}
