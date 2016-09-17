package org.rublin.web;

import org.rublin.model.Zone;
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
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
@Controller
@RequestMapping(value = "/zones")
public class ZoneController {

    public static final Logger LOG = getLogger(ZoneController.class);

    @Autowired
    private ZoneService zoneService;

    @RequestMapping(method = RequestMethod.GET)
    public String zoneList(Model model) {
        LOG.debug("requested all zones");
        model.addAttribute("zoneList", zoneService.getAll());
        return "zoneList";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        LOG.info("Zone with id: {} deleted", id);
        zoneService.delete(Integer.valueOf(id));
        return "redirect:/zones";
    }
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public String update(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        model.addAttribute("zone", zoneService.get(Integer.valueOf(id)));
        model.addAttribute("zoneList", zoneService.getAll());
        return "zoneList";
    }

    @RequestMapping(value = "/secure", method = RequestMethod.POST)
    public String secure(HttpServletRequest request) {
        Zone zone;
        String secure = request.getParameter("secure");
        String id = request.getParameter("id");
        if (secure != null) {
            zone = zoneService.get(Integer.valueOf(id));
            zoneService.setSecure(zone, Boolean.valueOf(secure));
        }
        LOG.info("zone {} is secured to {}", id, secure);
        return "redirect:/zones";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createOrUpdate(HttpServletRequest request) {
        Zone zone;
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String shortName = request.getParameter("shortName");
        LOG.info("id is: " + id);
        if (id.isEmpty()) {
            zone = new Zone(name, shortName);
            LOG.info("new zone {} added", zone);
        } else {
            zone = new Zone(Integer.valueOf(id), name, shortName);
            LOG.info("zone {} changed", zone);
        }
        zoneService.save(zone);
        return "redirect:/zones";
    }

}
