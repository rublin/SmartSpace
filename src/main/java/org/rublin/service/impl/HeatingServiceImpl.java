package org.rublin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.model.SystemConfig;
import org.rublin.service.HeatingService;
import org.rublin.service.SystemConfigService;
import org.rublin.to.HeatingResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Objects;

import static java.lang.String.format;
import static org.rublin.model.ConfigKey.PUMP;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeatingServiceImpl implements HeatingService {

    private final SystemConfigService systemConfigService;

    @Value("${pump.relay.ip}")
    private String ip;

    @Override
    public String pump(boolean enable) {
        systemConfigService.put(new SystemConfig(PUMP, String.valueOf(enable)));

        String state = enable ? "ON" : "OFF";
        if (current().isGlobalStatus() != enable) {
            final String url = format("http://%s/relay=%s",
                    ip,
                    state);
            log.debug("Send relay url: {}", url);
            HeatingResponseDto result = new RestTemplate().getForObject(url, HeatingResponseDto.class);
            log.info("Send relay {}. Response is {}", state, result);
            return String.valueOf(result.isGlobalStatus());
        } else {
            log.debug("Relay is already {}", state);
        }
        return "fail";
    }

    @Override
    public HeatingResponseDto current() {
        return new RestTemplate().getForObject(format("http://%s", ip), HeatingResponseDto.class);
    }

    @PostConstruct
    private void init() {
        String pump = systemConfigService.get(PUMP);
        if (Objects.nonNull(pump)) {
            pump(Boolean.valueOf(pump));
        }
    }
}
