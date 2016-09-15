package org.rublin.web;

import org.rublin.service.ZoneService;
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
@RequestMapping(value = "/zones")
public class ZoneController {

    public static final Logger LOG = getLogger(ZoneController.class);

    @Autowired
    private ZoneService zoneService;

    @RequestMapping(method = RequestMethod.GET)
    public String zoneList(Model model) {
        LOG.info(zoneService.getAll().toString());
        model.addAttribute("zoneList", zoneService.getAll());
        return "zoneList";
    }

}
