package org.rublin.util;

import org.rublin.model.user.Role;
import org.rublin.model.user.User;
import org.rublin.to.UserTo;

/**
 * Created by Ruslan Sheremet (rublin) on 04.09.2016.
 */
public class UserUtil {

    public static User createNewFromTo(UserTo newUser) {
        return new User(null, newUser.getFirstName(), newUser.getLastName(), newUser.getEmail().toLowerCase(), newUser.getPassword(), newUser.getMobile(), newUser.getTelegramName(), Role.ROLE_USER);
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getTelegramName(), user.getMobile());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setFirstName(userTo.getFirstName());
        user.setLastName(userTo.getLastName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        user.setTelegramName(userTo.getTelegramName());
        user.setMobile(userTo.getMobile());
        return user;
    }

    public static User prepareToSave(User user) {
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
