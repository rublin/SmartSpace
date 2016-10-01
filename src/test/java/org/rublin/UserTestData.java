package org.rublin;

import org.rublin.matcher.ModelMatcher;
import org.rublin.model.user.Role;
import org.rublin.model.user.User;
import org.rublin.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;

/**
 * Created by Ruslan Sheremet (rublin) on 30.09.2016.
 */
public class UserTestData {

    private static final Logger LOG = LoggerFactory.getLogger(UserTestData.class);

    public static final int USER_ID = 100;
    public static final int ADMIN_ID = USER_ID + 1;

    public static final User USER = new User(USER_ID, "Bohdan", "Khmelnytsky", Collections.singleton(Role.ROLE_USER), "user@gmail.com", "P@ssw0rd", "+380957654321", "Helenko");
    public static final User ADMIN = new User(ADMIN_ID, "Ivan", "Mazepa", Collections.singleton(Role.ROLE_ADMIN), "admin@gmail.com", "P@ssw0rd", "++380951234567", "rublinua", 241931659);

    public static final ModelMatcher<User> MATCHER = new ModelMatcher<>(User.class,
            (expected, actual) -> {
                if (expected == actual) {
                    return true;
                }
                boolean cmp = comparePassword(expected.getPassword(), actual.getPassword())
                        && Objects.equals(expected.getId(), actual.getId())
                        && Objects.equals(expected.getFirstName(), actual.getFirstName())
                        && Objects.equals(expected.getEmail(), actual.getEmail())
                        && Objects.equals(expected.getMobile(), actual.getMobile())
                        && Objects.equals(expected.isEnabled(), actual.isEnabled())
                        && Objects.equals(expected.getRoles(), actual.getRoles());
                return cmp;
            }
    );


    private static boolean comparePassword(String rawOrEncodedPassword, String password) {
        if (PasswordUtil.isEncoded(rawOrEncodedPassword)) {
            return rawOrEncodedPassword.equals(password);
        } else if (!PasswordUtil.isMatch(rawOrEncodedPassword, password)) {
            LOG.error("Password " + password + " doesn't match encoded " + password);
            return false;
        }
        return true;
    }
}
