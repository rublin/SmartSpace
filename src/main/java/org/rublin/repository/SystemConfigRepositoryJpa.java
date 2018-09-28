package org.rublin.repository;

import org.rublin.model.ConfigKey;
import org.rublin.model.SystemConfig;
import org.springframework.data.repository.CrudRepository;

public interface SystemConfigRepositoryJpa extends CrudRepository<SystemConfig, ConfigKey> {
}
