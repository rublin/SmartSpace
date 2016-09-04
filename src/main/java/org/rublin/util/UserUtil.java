package org.rublin.util;

import org.rublin.model.user.User;

/**
 * Created by Ruslan Sheremet (rublin) on 04.09.2016.
 */
public class UserUtil {
    public static User prepareToSave(User user) {
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
