package org.rublin.repository;

import org.rublin.model.user.Role;
import org.rublin.model.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepositoryJpa extends PagingAndSortingRepository<User, Integer> {
    User findByEmail(String email);

    User findByTelegramId(int telegramId);

    User findByTelegramName(String name);

    User findByMobile(String mobile);

    List<User> findByRoles(Role role);

    List<User> findByEnabled(Boolean enabled);
}
