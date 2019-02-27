package org.rublin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.events.OnHeatingEvent;
import org.rublin.events.OnHeatingStopEvent;
import org.rublin.service.delayed.DelayQueueService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListenerServiceImpl {

    private final DelayQueueService delayQueueService;

    @Async
    @EventListener
    public void heatingListener(OnHeatingEvent event) {
        log.info("Received heating {} event with delay {} sec", event.isPumpStatus(), event.getStartTime());
        delayQueueService.put(event);
    }

    @Async
    @EventListener
    public void heatingStopListener(OnHeatingStopEvent event) {
        log.info("Received heating stop event. All the delayed processes will be deleted");
        delayQueueService.clearHeating();
    }
}
