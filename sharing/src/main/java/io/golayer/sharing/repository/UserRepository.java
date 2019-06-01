package io.golayer.sharing.repository;

import io.golayer.sharing.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
