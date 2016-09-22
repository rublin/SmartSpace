package org.rublin.web;

import org.rublin.model.user.Role;
import org.rublin.model.user.User;
import org.rublin.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 18.09.2016.
 */
@Controller
@RequestMapping(value = "users")
public class UserController {

    private static final Logger LOG = getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String userList(Model model) {
        model.addAttribute("userList", userService.getAll());
        LOG.info("show all users");
        return "userList";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        userService.delete(Integer.valueOf(id));
        LOG.info("user with id {} deleted", id);
        return "redirect:/users";
    }

    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    public String enable(HttpServletRequest request) {
        User user = getUser(request.getParameter("id"));
        user.setEnabled(user.isEnabled() ? false : true);
        userService.save(user);
        LOG.info("user {} was {}", user, user.isEnabled() ? "enabled" : "disabled");
        return "redirect:/users";
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public String select(HttpServletRequest request, Model model) {
        User user = getUser(request.getParameter("id"));
        model.addAttribute("user", user);
        model.addAttribute("userList", userService.getAll());
        return "userList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createOrUpdate(HttpServletRequest request) {
        String  id = request.getParameter("id");
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String role = request.getParameter("admin");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String mobile = request.getParameter("mobile");
        String telegramName = request.getParameter("telegram_name");
        User user;
        if (role == null) {
            role = "ROLE_USER";
        }
        Set<Role> roles = new HashSet<>(Arrays.asList(Role.valueOf(role)));
        if (id.isEmpty()) {
            user = new User(fname, lname, roles, email, password, mobile, telegramName);
            LOG.info("new user {} created", user);
        } else {
            user = new User(Integer.valueOf(id), fname, lname, roles, email, password, mobile, telegramName);
            LOG.info("user {} was changed", user);
        }
        userService.save(user);
        return "redirect:/users";
    }
    private User getUser(String id) {
        return userService.get(Integer.valueOf(id));
    }
}
