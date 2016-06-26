package web.rest;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.StateService;

import java.util.List;

/**
 * Created by Sheremet on 16.06.2016.
 */
@Controller
public class StateRestController {
    @Autowired
    private StateService service;
    public void save (Trigger trigger, Event event) {
        service.save(trigger, event);
    }
    public List<Event> getAll (Trigger trigger) {
        return service.getAll(trigger);
    }
}
