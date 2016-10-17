package org.rublin.web;

import org.rublin.model.Camera;
import org.rublin.model.Zone;
import org.rublin.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
@Controller
@RequestMapping(value = "/camera")
public class CameraController extends AbstractCameraController {

    @Autowired
    private ZoneService zoneService;

    @RequestMapping(method = RequestMethod.GET)
    public String cameraList(Model model) {
        model.addAttribute("cameraList", super.getAll());
        model.addAttribute("zoneList", zoneService.getAll());
        return "cameraList";
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public String update(HttpServletRequest request, Model model) {
        model.addAttribute("camera", super.get(Integer.valueOf(request.getParameter("id"))));
        model.addAttribute("cameraList", super.getAll());
        model.addAttribute("zoneList", zoneService.getAll());
        return "cameraList";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String createOrUpdate(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String url = request.getParameter("url");
        String ip = request.getParameter("ip");
        Zone zone = zoneService.get(Integer.valueOf(request.getParameter("zoneId")));
        super.createOrUpdate(new Camera(name, ip, login, password, url, zone), zone, id);
        return "redirect:/camera";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        super.delete(Integer.valueOf(id));
        return "redirect:/camera";
    }
}
