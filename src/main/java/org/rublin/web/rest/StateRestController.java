package org.rublin.web.rest;

import org.rublin.model.*;
import org.rublin.model.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.rublin.service.StateService;

import java.util.List;

/**
 * Created by Sheremet on 16.06.2016.
 */
//@Controller
public class StateRestController {
    @Autowired
    private StateService service;

    public void save (Trigger trigger, Event event) {
        service.save(trigger, event);
    }
    public List<Event> get (Trigger trigger) {
        return service.get(trigger);
    }
    public List<Event> getAll() {
        return service.getAll();
    }
}
