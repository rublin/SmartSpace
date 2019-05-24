package org.rublin.service;

import org.rublin.model.user.User;
import org.rublin.util.exception.NotFoundException;

import java.util.List;

/**
 * Created by Ruslan Sheremet (rublin) on 04.09.2016.
 */
public interface UserService {
    User save (User user);
    void delete(int id) throws NotFoundException;
    User get(int id) throws NotFoundException;
    User getByEmail(String email) throws NotFoundException;
    User getByTelegramId(int telegramId) throws NotFoundException;
    User getByTelegramName(String name) throws NotFoundException;
    User getByMobile(String mobile) throws NotFoundException;
    List<User> getAll();

    List<User> getAdmins();
    void update(User user);
    void enable(int id, boolean enable);
}
