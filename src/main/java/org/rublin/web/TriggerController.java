package org.rublin.web;

import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.Zone;
import org.rublin.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Ruslan Sheremet (rublin) on 11.09.2016.
 */
@Controller
@RequestMapping(value = "/triggers")
public class TriggerController extends AbstractTriggerController {

    @Autowired
    private ZoneService zoneService;

    @RequestMapping(method = RequestMethod.GET)
    public String triggerList(Model model) {
        model.addAttribute("triggerList", super.getAll());
        model.addAttribute("zoneList", zoneService.getAll());
        LOG.info("show all triggers");
        return "triggerList";
    }
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        super.delete(Integer.valueOf(id));
        return "redirect:/triggers";
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public String select(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        model.addAttribute("trigger", super.get(Integer.valueOf(id)));
        model.addAttribute("triggerList", super.getAll());
        model.addAttribute("zoneList", zoneService.getAll());
        return "triggerList";
    }
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String updateOrCreate(HttpServletRequest request) {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        boolean secureTrigger = request.getParameter("secureTrigger") != null;
        Zone zone = zoneService.get(Integer.valueOf(request.getParameter("zoneId")));
        String min = request.getParameter("minThreshold");
        String max = request.getParameter("maxThreshold");
        Trigger trigger;
        if (type.equals("digital")) {
            trigger = new Trigger(name, zone, Type.DIGITAL, secureTrigger);
        } else {
            trigger = new Trigger(name, zone, Type.ANALOG, secureTrigger, Double.valueOf(request.getParameter("minThreshold")), Double.valueOf(request.getParameter("maxThreshold")));
        }
        super.createOrUpdate(trigger, zone, id);
        return "redirect:/triggers";
    }
}
