package org.rublin.web;

import org.rublin.model.user.Role;
import org.rublin.model.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ruslan Sheremet (rublin) on 18.09.2016.
 */
@Controller
@RequestMapping(value = "users")
public class UserController extends AbstractUserController {

    @RequestMapping(method = RequestMethod.GET)
    public String userList(Model model) {
        model.addAttribute("userList", super.getAll());
        return "userList";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        super.delete(Integer.valueOf(id));
        return "redirect:/users";
    }

    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    public String enable(HttpServletRequest request) {
        super.enable(Integer.valueOf(request.getParameter("id")));
        return "redirect:/users";
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public String select(HttpServletRequest request, Model model) {
        model.addAttribute("user", super.get(Integer.valueOf(request.getParameter("id"))));
        model.addAttribute("userList", super.getAll());
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
        if (role == null) {
            role = "ROLE_USER";
        }
        Set<Role> roles = new HashSet<>(Arrays.asList(Role.valueOf(role)));
        super.createOrUpdate(new User(fname, lname, roles, email, password, mobile, telegramName), id);
        return "redirect:/users";
    }
}
