package org.rublin.service;

import org.rublin.model.user.User;
import org.rublin.repository.UserRepository;
import org.rublin.util.UserUtil;
import org.rublin.util.exception.ExceptionUtil;
import org.rublin.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created by Ruslan Sheremet (rublin) on 04.09.2016.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User save(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }

    @Override
    public void delete(int id) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public User get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        Objects.requireNonNull(email, "Email must not be empty");
        return ExceptionUtil.checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Override
    public User getByTelegramId(int telegramId) throws NotFoundException {
        Objects.requireNonNull(telegramId, "TelegramId must not be empty");
        return ExceptionUtil.checkNotFound(repository.getByTelegramId(telegramId), "telegramId=" + telegramId);
    }

    @Override
    public User getByTelegramName(String name) throws NotFoundException {
        Objects.requireNonNull(name, "TelegramName must not be empty");
        return ExceptionUtil.checkNotFound(repository.getByTelegramName(name), "telegramName=" + name);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public void update(User user) {
        repository.save(UserUtil.prepareToSave(user));
    }

    @Override
    @Transactional
    public void enable(int id, boolean enable) {
        User user = get(id);
        user.setEnabled(enable);
        repository.save(user);
    }
}
