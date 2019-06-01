package org.rublin.service.impl;

import com.google.common.collect.Lists;
import org.rublin.AuthorizedUser;
import org.rublin.model.user.Role;
import org.rublin.model.user.User;
import org.rublin.repository.UserRepositoryJpa;
import org.rublin.service.UserService;
import org.rublin.util.UserUtil;
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
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public User get(int id) {
        return repository.findById(id).get();
    }

    @Override
    public User getByEmail(String email) {
        Objects.requireNonNull(email, "Email must not be empty");
        return repository.findByEmail(email);
    }

    @Override
    public User getByTelegramId(int telegramId) {
        Objects.requireNonNull(telegramId, "TelegramId must not be empty");
        return repository.findByTelegramId(telegramId);
    }

    @Override
    public User getByTelegramName(String name) {
        Objects.requireNonNull(name, "TelegramName must not be empty");
        return repository.findByTelegramName(name);
    }

    @Override
    public User getByMobile(String mobile) {
        Objects.requireNonNull(mobile, "Mobile number must not be empty");
        return repository.findByMobile(mobile);
    }

    @Override
    public List<User> getAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public List<User> getAdmins() {
        return repository.findByRoles(Role.ROLE_ADMIN);
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
