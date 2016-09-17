package org.rublin.web;

import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.Zone;
import org.rublin.service.TriggerService;
import org.rublin.service.ZoneService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 11.09.2016.
 */
@Controller
@RequestMapping(value = "/triggers")
public class TriggerController {

    private static final Logger LOG = getLogger(TriggerController.class);

    @Autowired
    private TriggerService triggerService;
    @Autowired
    private ZoneService zoneService;

    @RequestMapping(method = RequestMethod.GET)
    public String triggerList(Model model) {
        model.addAttribute("triggerList", triggerService.getAll());
        model.addAttribute("zoneList", zoneService.getAll());
        LOG.info("show all triggers");
        return "triggerList";
    }
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        triggerService.delete(Integer.valueOf(id));
        LOG.info("trigger with id {} deleted", id);
        return "redirect:/triggers";
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public String select(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        Trigger trigger = triggerService.get(Integer.valueOf(id));
        model.addAttribute("trigger", trigger);
        model.addAttribute("triggerList", triggerService.getAll());
        model.addAttribute("zoneList", zoneService.getAll());
        return "triggerList";
    }
    @RequestMapping(method = RequestMethod.POST)
    public String updateOrCreate(HttpServletRequest request) {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        boolean secureTrigger = request.getParameter("secureTrigger") != null;
        LOG.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!checkbox is: {}", request.getParameter("secureTrigger"));
        Zone zone = zoneService.get(Integer.valueOf(request.getParameter("zoneId")));
        String min = request.getParameter("minThreshold");
        String max = request.getParameter("maxThreshold");
        Trigger trigger;
        LOG.debug("id {}, name {}, type {}, secure {}, min {}, max {}, zone {}", id, name, type, secureTrigger, min, max, zone.getName());
        if (id.isEmpty()) {
            if (type.equals("digital")) {
                trigger = new Trigger(name, zone, Type.DIGITAL, secureTrigger);
            } else {
                trigger = new Trigger(name, zone, Type.ANALOG, secureTrigger, Double.valueOf(request.getParameter("minThreshold")), Double.valueOf(request.getParameter("maxThreshold")));
            }
            LOG.info("new trigger {} added in zone {}", trigger, zone.getName());
        } else {
            trigger = triggerService.get(Integer.valueOf(id));
            trigger.setName(name);
            trigger.setSecure(secureTrigger);
            trigger.setZone(zone);
            if (trigger.getType() == Type.ANALOG) {
                trigger.setMinThreshold(Double.valueOf(min));
                trigger.setMaxThreshold(Double.valueOf(max));
            }
            LOG.info("trigger {} changed in zone {}", trigger, zone.getName());
        }
        triggerService.save(trigger, zone);
        return "redirect:/triggers";
    }
}
