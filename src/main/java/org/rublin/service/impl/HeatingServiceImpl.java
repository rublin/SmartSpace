package org.rublin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.rublin.service.HeatingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

@Slf4j
@Service
public class HeatingServiceImpl implements HeatingService {

    private AtomicBoolean currentState = new AtomicBoolean(false);

    @Value("${pump.relay.ip}")
    private String ip;

    @Override
    public void pump(boolean enable) {
        String state = enable ? "ON" : "OFF";
        if (currentState.get() != enable) {
            final String url = format("http://%s/relay=%s",
                    ip,
                    state);
            log.debug("Send relay url: {}", url);
            String result = new RestTemplate().getForObject(url, String.class);
            log.info("Send relay {}. Response is {}", state, result);
            currentState.set(enable);
        } else {
            log.debug("Relay is already {}", state);
        }
    }

    @Override
    public boolean current() {
        return currentState.get();
    }
}
