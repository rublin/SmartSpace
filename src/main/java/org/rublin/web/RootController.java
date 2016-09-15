package org.rublin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
@Controller
public class RootController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return "index";
    }
}
