package org.rublin.web;

import org.rublin.model.Zone;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
@Controller
@RequestMapping(value = "/zones")
public class ZoneController extends AbstractZoneController{

    @RequestMapping(method = RequestMethod.GET)
    public String zoneList(Model model) {
        LOG.debug("requested all zones");
        model.addAttribute("zoneList", super.getAll());
        return "zoneList";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        super.delete(Integer.valueOf(id));
        return "redirect:/zones";
    }
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public String update(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        model.addAttribute("zone", super.get(Integer.valueOf(id)));
        model.addAttribute("zoneList", super.getAll());
        return "zoneList";
    }

    @RequestMapping(value = "/secure", method = RequestMethod.POST)
    public String secure(HttpServletRequest request) {
        String secure = request.getParameter("secure");
        String id = request.getParameter("id");
        if (secure != null) {
            super.setSecure(Integer.valueOf(id), Boolean.valueOf(secure));
        }
        return "redirect:/zones";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String createOrUpdate(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String shortName = request.getParameter("shortName");
        super.createOrUpdate(new Zone(name, shortName), id);
        return "redirect:/zones";
    }

}
