package org.rublin.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.model.event.TemperatureEvent;
import org.rublin.model.sensor.TemperatureSensor;
import org.rublin.repository.TemperatureEventRepositoryJpa;
import org.rublin.repository.TemperatureSensorRepositoryJpa;
import org.rublin.service.TemperatureService;
import org.rublin.service.ThingSpeakService;
import org.rublin.to.AddEventRequest;
import org.rublin.to.ThingSpeakChannelSettingDto;
import org.rublin.to.ThingSpeakUploadDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.hibernate.internal.util.StringHelper.isNotEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {

    private final TemperatureSensorRepositoryJpa temperatureRepository;
    private final TemperatureEventRepositoryJpa eventRepository;
    private final ThingSpeakService thingSpeakService;

    @Value("${thingspeak.api.key.config}")
    private String apiKeyConfig;

    @Value("${thingspeak.api.key.write}")
    private String apiKeyWrite;

    @Value("${thingspeak.api.channel}")
    private int channelId;

    @Override
    public TemperatureSensor save(TemperatureSensor sensor) {
        int fieldId = 0;
        Set<Integer> fieldIdSet = getAll().stream()
                .map(TemperatureSensor::getFieldId)
                .collect(toSet());
        for (int i = 1; i <= 8; i++) {
            if (!fieldIdSet.contains(i)) {
                fieldId = i;
                break;
            }
        }
        if (fieldId > 0) {
            if (Objects.isNull(sensor.getThingSpeakApiKey()) || Objects.isNull(sensor.getThingSpeakChannelId())) {
                sensor.setThingSpeakApiKey(apiKeyWrite);
                sensor.setThingSpeakChannelId(channelId);
            }
            sensor.setFieldId(fieldId);
            thingSpeakService.channelSetting(ThingSpeakChannelSettingDto.builder()
                    .apiKey(apiKeyConfig)
                    .fieldId(fieldId)
                    .fieldName(sensor.getName())
                    .channelId(channelId)
                    .build());
        }

        return temperatureRepository.save(sensor);
    }

    @Override
    public TemperatureSensor get(int id) {
        return temperatureRepository.findById(id).get();
    }

    @Override
    public List<TemperatureSensor> getAll() {
        return Lists.newArrayList(temperatureRepository.findAll());
    }

    @Override
    public void addEvent(AddEventRequest eventRequest) {
        TemperatureSensor sensor = temperatureRepository.findById(eventRequest.getSensorId()).get();
        checkNotNull(sensor, "Sensor couldn't be null");
        TemperatureEvent event = TemperatureEvent.builder()
                .sensor(sensor)
                .temperature(new BigDecimal(eventRequest.getValue()))
                .time(LocalDateTime.now())
                .build();
        String apiKey = sensor.getThingSpeakApiKey();
        Integer channelId = sensor.getThingSpeakChannelId();
        if (Objects.nonNull(channelId) && isNotEmpty(apiKey)) {
            thingSpeakService.upload(ThingSpeakUploadDto.builder()
                    .apiKey(sensor.getThingSpeakApiKey())
                    .fieldId(sensor.getFieldId())
                    .value(eventRequest.getValue())
                    .build());
        }
        TemperatureEvent saved = eventRepository.save(event);
        log.info("Saved temperature {}C for sensor {} with id {}", saved.getTemperature(), sensor.getName(), saved.getId());
    }
}
