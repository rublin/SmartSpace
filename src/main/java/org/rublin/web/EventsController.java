package org.rublin.web;

import org.rublin.service.StateService;
import org.rublin.service.TriggerService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
@Controller
@RequestMapping(value = "/events")
public class EventsController {

    public static final Logger LOG = getLogger(EventsController.class);

    @Autowired
    private StateService eventService;

    @Autowired
    private TriggerService triggerService;

    @RequestMapping(method = RequestMethod.GET)
    public String eventList(Model model) {
        model.addAttribute("eventList", eventService.getAll());
        model.addAttribute("triggerList", triggerService.getAll());
        return "eventList";
    }


}
