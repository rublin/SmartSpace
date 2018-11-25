package org.rublin.repository;

import org.rublin.model.event.TemperatureEvent;
import org.springframework.data.repository.CrudRepository;

public interface TemperatureEventRepositoryJpa extends CrudRepository<TemperatureEvent, Integer> {
}
