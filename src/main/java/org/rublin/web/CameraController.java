package org.rublin.web;

import org.rublin.model.Camera;
import org.rublin.model.Zone;
import org.rublin.service.CameraService;
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
@RequestMapping(value = "/camera")
public class CameraController {

    public static final Logger LOG = getLogger(CameraController.class);

    @Autowired
    private CameraService cameraService;
    @Autowired
    private ZoneService zoneService;

    @RequestMapping(method = RequestMethod.GET)
    public String cameraList(Model model) {
        model.addAttribute("cameraList", cameraService.getAll());
        model.addAttribute("zoneList", zoneService.getAll());
        return "cameraList";
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public String update(HttpServletRequest request, Model model) {
        Camera camera = getCamera(request.getParameter("id"));
        model.addAttribute("camera", camera);
        model.addAttribute("cameraList", cameraService.getAll());
        model.addAttribute("zoneList", zoneService.getAll());
        return "cameraList";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public String createOrUpdate(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String url = request.getParameter("url");
        String ip = request.getParameter("ip");
        Zone zone = zoneService.get(Integer.valueOf(request.getParameter("zoneId")));
        Camera camera;
        if (id.isEmpty()) {
            camera = new Camera(name, ip, login, password, url, zone);
            LOG.info("new camera {} added", camera);
        } else {
            camera = new Camera(Integer.valueOf(id), name, ip, login, password, url, zone);
            LOG.info("camera {} updated", camera);
        }
        cameraService.save(camera, zone);
        return "redirect:/camera";
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        cameraService.delete(Integer.valueOf(id));
        LOG.info("camera with id {} deleted", id);
        return "redirect:/camera";
    }

    private Camera getCamera(String id) {
        return cameraService.get(Integer.valueOf(id));
    }
}
