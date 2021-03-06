package org.rublin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.model.ConfigKey;
import org.rublin.model.SystemConfig;
import org.rublin.repository.SystemConfigRepositoryJpa;
import org.rublin.service.SystemConfigService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepositoryJpa configRepository;

    @Override
    public String get(ConfigKey key) {
        log.info("Looking for system config with {} key", key.name());
        Optional<SystemConfig> optionalSystemConfig = configRepository.findById(key);
        return optionalSystemConfig.map(SystemConfig::getValue).orElse(null);
    }

    @Override
    public void put(SystemConfig config) {
        log.info("Save system config: {}", config);
        configRepository.save(config);
    }
}
