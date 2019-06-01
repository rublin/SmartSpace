package io.golayer.sharing.service;

import io.golayer.sharing.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User create(User user);
}
