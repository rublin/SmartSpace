package org.rublin.web;

import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.rublin.service.EventService;
import org.rublin.service.TriggerService;
import org.rublin.util.TimeUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
@Controller
@RequestMapping(value = "/events")
public class EventsController {

    public static final Logger LOG = getLogger(EventsController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private TriggerService triggerService;

    @RequestMapping(method = RequestMethod.GET)
    public String eventList(Model model) {
        model.addAttribute("eventList", eventService.getAll());
        model.addAttribute("triggerList", triggerService.getAll());
        return "eventList";
    }

    @RequestMapping(value = "/byTrigger", method = RequestMethod.GET)
    public String getByTrigger(HttpServletRequest request, Model model) {
        Trigger trigger = getTrigger(request.getParameter("triggerId"));
        model.addAttribute("eventList", eventService.get(trigger));
        model.addAttribute("triggerList", triggerService.getAll());
        LOG.info("select events by trigger {}", trigger);
        return "eventList";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String create(HttpServletRequest request) {
        String state = request.getParameter("state");
        Trigger trigger = getTrigger(request.getParameter("triggerId"));
        Event event;
        if (trigger.getType() == Type.ANALOG) {
            event = new AnalogEvent(trigger, Double.valueOf(state));
        } else {
            event = new DigitEvent(trigger, Boolean.valueOf(state));
        }
        eventService.save(trigger, event);
        LOG.info("new event {} added", event);
        return "OK";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String getBetween(HttpServletRequest request, Model model) {
        LocalDateTime from = TimeUtil.parseLocalDateTime(resetParam("from", request));
        LocalDateTime to = TimeUtil.parseLocalDateTime(resetParam("to", request));
        model.addAttribute("eventList", eventService.getBetween(from, to));
        model.addAttribute("triggerList", triggerService.getAll());
        return "eventList";
    }

    private Trigger getTrigger(String id) {
        return triggerService.get(Integer.valueOf(id));
    }

    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }
}
