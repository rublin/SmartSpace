package org.rublin.repository;

import org.rublin.model.user.User;

import java.util.List;

/**
 * Created by Ruslan Sheremet (rublin) on 04.09.2016.
 */
public interface UserRepository {
    User save (User user);
    boolean delete(int id);
    User get(int id);
    User getByEmail(String email);
    User getByTelegramId(int telegramId);
    User getByTelegramName(String name);
    List<User> getAll();
}
