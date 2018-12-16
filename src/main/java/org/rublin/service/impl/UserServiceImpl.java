package org.rublin.service.impl;

import com.google.common.collect.Lists;
import org.rublin.AuthorizedUser;
import org.rublin.model.user.User;
import org.rublin.repository.UserRepositoryJpa;
import org.rublin.service.UserService;
import org.rublin.util.UserUtil;
import org.rublin.util.exception.ExceptionUtil;
import org.rublin.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created by Ruslan Sheremet (rublin) on 04.09.2016.
 */
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService{

    @Autowired
    private UserRepositoryJpa repository;

    @Override
    public User save(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }

    @Override
    public void delete(int id) throws NotFoundException {
        repository.delete(id);
    }

    @Override
    public User get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.findOne(id), id);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        Objects.requireNonNull(email, "Email must not be empty");
        return ExceptionUtil.checkNotFound(repository.findByEmail(email), "email=" + email);
    }

    @Override
    public User getByTelegramId(int telegramId) throws NotFoundException {
        Objects.requireNonNull(telegramId, "TelegramId must not be empty");
        return ExceptionUtil.checkNotFound(repository.findByTelegramId(telegramId), "telegramId=" + telegramId);
    }

    @Override
    public User getByTelegramName(String name) throws NotFoundException {
        Objects.requireNonNull(name, "TelegramName must not be empty");
        return ExceptionUtil.checkNotFound(repository.findByTelegramName(name), "telegramName=" + name);
    }

    @Override
    public User getByMobile(String mobile) throws NotFoundException {
        Objects.requireNonNull(mobile, "Mobile number must not be empty");
        return ExceptionUtil.checkNotFound(repository.findByMobile(mobile), "mobile=" + mobile);
    }

    @Override
    public List<User> getAll() {
        return Lists.newArrayList(repository.findAll());
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
