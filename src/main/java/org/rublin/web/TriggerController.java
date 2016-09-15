package org.rublin.web;

import org.rublin.model.Trigger;
import org.rublin.model.Type;
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
        return "triggerList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateOrCreate(HttpServletRequest request) {
        LOG.info(request.toString());
        String id = request.getParameter("id");
//        String type = request.getParameter("type");
        String name = request.getParameter("name");
        String secure = request.getParameter("secure");
        boolean secureTrigger = request.getParameter("secureTrigger") == null ? Boolean.FALSE : Boolean.TRUE;
        Trigger trigger = null;
        if (id.isEmpty()) {
            trigger = new Trigger(name, Type.DIGITAL);
        } else {
            trigger = new Trigger(Integer.valueOf(id), name, Type.DIGITAL);
        }

        triggerService.save(trigger, zoneService.get(CurrentZone.getId()));
        return "redirect:/triggers";
    }
}
