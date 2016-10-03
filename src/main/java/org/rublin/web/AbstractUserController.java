package org.rublin.web;

import org.rublin.AuthorizedUser;
import org.rublin.model.user.User;
import org.rublin.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 02.10.2016.
 */
public class AbstractUserController {
    protected final Logger LOG = getLogger(getClass());

    @Autowired
    private UserService userService;

    public User get(int id) {
        LOG.debug("get user with id {}", id);
        return userService.get(id);
    }

    public List<User> getAll() {
        LOG.debug("get all users");
        return userService.getAll();
    }

    public void delete(int id) {
        LOG.info("delete user with id {} by user {}", id, AuthorizedUser.log());
        userService.delete(id);
    }

    public User createOrUpdate(User user, String id) {
        if (id == null || id.equals("")) {
            LOG.info("user {} added by user {}", user, AuthorizedUser.log());
        } else {
            user.setId(Integer.valueOf(id));
            LOG.info("user {} changed by user {}", user, AuthorizedUser.log());
        }
        return userService.save(user);
    }

    public void enable(int id) {
        User user = userService.get(id);
        user.setEnabled(user.isEnabled() ? false : true);
        userService.save(user);
        LOG.info("user {} was {} by user {}", user, user.isEnabled() ? "enabled" : "disabled", AuthorizedUser.log());
    }
}
