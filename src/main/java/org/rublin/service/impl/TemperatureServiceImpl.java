package org.rublin.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.model.event.TemperatureEvent;
import org.rublin.model.sensor.TemperatureSensor;
import org.rublin.repository.TemperatureEventRepositoryJpa;
import org.rublin.repository.TemperatureSensorRepositoryJpa;
import org.rublin.service.TemperatureService;
import org.rublin.to.AddEventRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hibernate.internal.util.StringHelper.isNotEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {

    private final TemperatureSensorRepositoryJpa temperatureRepository;
    private final TemperatureEventRepositoryJpa eventRepository;

    @Override
    public TemperatureSensor save(TemperatureSensor sensor) {
        //ToDo create Field in ThingSpeak
        return temperatureRepository.save(sensor);
    }

    @Override
    public TemperatureSensor get(int id) {
        return temperatureRepository.findOne(id);
    }

    @Override
    public List<TemperatureSensor> getAll() {
        return Lists.newArrayList(temperatureRepository.findAll());
    }

    @Override
    public void addEvent(AddEventRequest eventRequest) {
        TemperatureSensor sensor = temperatureRepository.findOne(eventRequest.getSensorId());
        checkNotNull(sensor, "Sensor couldn't be null");
        TemperatureEvent event = TemperatureEvent.builder()
                .sensor(sensor)
                .temperature(new BigDecimal(eventRequest.getValue()))
                .time(LocalDateTime.now())
                .build();
        String apiKey = sensor.getThingSpeakApiKey();
        Integer channelId = sensor.getThingSpeakChannelId();
        if (Objects.nonNull(channelId) && isNotEmpty(apiKey)) {
            //ToDo push value to ThingSpeak if needed
        }
        TemperatureEvent saved = eventRepository.save(event);
        log.info("Saved temperature {}C for sensor {} with id {}", saved.getTemperature(), sensor.getName(), saved.getId());
    }
}
