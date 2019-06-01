package org.rublin.repository;

import org.rublin.model.sensor.TemperatureSensor;
import org.springframework.data.repository.CrudRepository;

public interface TemperatureSensorRepositoryJpa extends CrudRepository<TemperatureSensor, Integer> {

}
