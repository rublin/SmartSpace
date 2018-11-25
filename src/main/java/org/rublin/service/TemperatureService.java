package org.rublin.service;

import org.rublin.model.sensor.TemperatureSensor;
import org.rublin.to.AddEventRequest;

import java.util.List;

public interface TemperatureService {
    TemperatureSensor save(TemperatureSensor sensor);

    TemperatureSensor get(int id);

    List<TemperatureSensor> getAll();

    void addEvent(AddEventRequest eventRequest);
}
