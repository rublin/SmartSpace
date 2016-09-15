package org.rublin.web;

import org.rublin.service.CameraService;
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
@RequestMapping(name = "/camera")
public class CameraController {

    public static final Logger LOG = getLogger(CameraController.class);

    @Autowired
    private CameraService cameraService;

    @RequestMapping(method = RequestMethod.GET)
    public String cameraList(Model model) {
        model.addAttribute("cameraList", cameraService.getAll());
        return "cameraList";
    }
}
