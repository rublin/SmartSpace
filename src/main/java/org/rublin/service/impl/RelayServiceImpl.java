package org.rublin.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.model.Relay;
import org.rublin.model.RelayState;
import org.rublin.repository.RelayRepository;
import org.rublin.service.RelayService;
import org.rublin.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelayServiceImpl implements RelayService {

    private final RelayRepository relayRepository;

    @Value("${relay.pump.waiting.seconds}")
    private Integer workSeconds;

    @Override
    public Relay find(Integer id) {
        Optional<Relay> optional = relayRepository.findById(id);
        return optional.orElseThrow(() -> new NotFoundException("Relay with id " + id + " not found"));
    }

    @Override
    public Relay find(String name) {
        return relayRepository.findByName(name).orElseThrow(() -> new NotFoundException("Relay not found"));
    }

    @Override
    public Relay create(Relay relay) {
        return relayRepository.save(relay);
    }

    @Override
    public RelayState getRelayStatus(Integer id) {
        Relay relay = find(id);
        return calculateState(relay);
    }

    @Override
    public RelayState changeRelayStatus(String name, int productivity) {
        Relay relay = find(name);
        relay.setProductivityPerCent(productivity);
        relayRepository.save(relay);

        log.info("Change relay {} state to {}", name, productivity);
        return calculateState(relay);
    }

    @Override
    public List<Relay> getAll() {
        return Lists.newArrayList(relayRepository.findAll(Sort.by("name")));
    }

    @Override
    public String toTelegram(Relay relay) {
        return format("Id: %d; Name: %s; Current: %s; State: %d%%", relay.getId(), relay.getName(), calculateState(relay), relay.getProductivityPerCent());
    }

    RelayState calculateState(Relay relay) {
        if (relay.getProductivityPerCent() == 0) {
            return RelayState.OFF;
        }

        int waitSeconds = (100 - relay.getProductivityPerCent()) * workSeconds / relay.getProductivityPerCent();
        long between = ChronoUnit.SECONDS.between(relay.getUpdated(), LocalDateTime.now());
        int currentPeriod = Math.toIntExact(Math.abs(between) % (workSeconds + waitSeconds));

        if (currentPeriod < workSeconds) {
            return RelayState.ON;
        }
        return RelayState.OFF;
    }
}
