package org.rublin.web.rest;

import org.rublin.model.*;
import org.rublin.model.event.Event;
import org.rublin.service.TriggerService;
import org.rublin.web.AbstractEventController;
import org.springframework.beans.factory.annotation.Autowired;
import org.rublin.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Sheremet on 16.06.2016.
 */
@RestController
@RequestMapping(EventRestController.REST_URL)
public class EventRestController extends AbstractEventController {
    static final String REST_URL = "/rest/event";

    @Autowired
    private TriggerService triggerService;

    /*@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void save (Trigger trigger, Event event) {
        super.create(trigger, event);
    }*/

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> get (@PathVariable("id") int triggerId) {
        return super.getByTrigger(triggerService.get(triggerId));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAll() {
        return super.getAll();
    }

    @RequestMapping(value = "/between", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getBetween(
            @RequestParam(value = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return super.getBetween(from, to);
    }

    @RequestMapping(value = "/alarmed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAlarmed() {
        return super.getAlarmed();
    }
}
