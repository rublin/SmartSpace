package org.rublin.web;

import org.rublin.model.Trigger;
import org.rublin.service.TemperatureService;
import org.rublin.service.TriggerService;
import org.rublin.to.AddEventRequest;
import org.rublin.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.http.util.Asserts.notEmpty;

/**
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
@Controller
@RequestMapping(value = "/events")
public class EventsController extends AbstractEventController {

    @Autowired
    private TriggerService triggerService;

    @Autowired
    private TemperatureService temperatureService;

    @RequestMapping(method = RequestMethod.GET)
    public String eventList(Model model) {
        model.addAttribute("eventList", super.getAll());
        model.addAttribute("triggerList", triggerService.getAll());
        return "eventList";
    }

    @RequestMapping(value = "/byTrigger", method = RequestMethod.GET)
    public String getByTrigger(HttpServletRequest request, Model model) {
        Trigger trigger = getTrigger(request.getParameter("triggerId"));
        model.addAttribute("eventList", super.getByTrigger(trigger));
        model.addAttribute("triggerList", triggerService.getAll());
        return "eventList";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String create(HttpServletRequest request) {
        String state = request.getParameter("state");
        Trigger trigger = getTrigger(request.getParameter("triggerId"));
        super.create(state, trigger);
        return "OK";
    }

    @RequestMapping(value = "/temperature/add", method = RequestMethod.GET)
    public String addTemperatureEvent(HttpServletRequest request) {
        AddEventRequest eventRequest = AddEventRequest.builder()
                .sensorId(Integer.valueOf(request.getParameter("triggerId")))
                .value(request.getParameter("state"))
                .build();
        checkNotNull(eventRequest.getSensorId(), "Sensor ID couldn't be null");
        LOG.info("Received temperature event with sensor id {} and value {}", eventRequest.getSensorId(), eventRequest.getValue());
        notEmpty(eventRequest.getValue(), "Temperature value");
        temperatureService.addEvent(eventRequest);
        return "OK";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String getBetween(HttpServletRequest request, Model model) {
        LocalDateTime from = TimeUtil.parseLocalDateTime(resetParam("from", request));
        LocalDateTime to = TimeUtil.parseLocalDateTime(resetParam("to", request));
        model.addAttribute("eventList", super.getBetween(from, to));
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
