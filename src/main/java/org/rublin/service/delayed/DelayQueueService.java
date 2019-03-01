package org.rublin.service.delayed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.events.OnHeatingEvent;
import org.rublin.message.NotificationMessage;
import org.rublin.model.ConfigKey;
import org.rublin.service.HeatingService;
import org.rublin.service.MediaPlayerService;
import org.rublin.service.SystemConfigService;
import org.rublin.service.TextToSpeechService;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class DelayQueueService {
    private final BlockingQueue<NotificationMessage> queue = new DelayQueue<>();
    private final BlockingQueue<OnHeatingEvent> heatingQueue = new DelayQueue<>();
    private final MediaPlayerService mediaPlayerService;
    private final SystemConfigService configService;
    private final TextToSpeechService textToSpeechService;
    private final HeatingService heatingService;


    public void put(NotificationMessage message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            log.warn("Failed to put a message: {}", e.getMessage());
        }
    }

    public void put(OnHeatingEvent event) {
        heatingQueue.clear();
        try {
            heatingQueue.put(event);
        } catch (InterruptedException e) {
            log.warn("Failed to put a heating event: {}", e.getMessage());
        }
    }

    public void clearHeating() {
        heatingQueue.clear();
    }

    public void take() {
        try {
            NotificationMessage notification = queue.take();
            log.info("Took {} message with {} delay", notification.getData(), notification.getStartTime());
            if (notification.getData().endsWith(".mp3")) {
                mediaPlayerService.play(notification.getData(), Integer.parseInt(configService.get(ConfigKey.TEXT_VOLUME)));
            } else {
                textToSpeechService.say(notification.getData(), "uk");
            }
        } catch (InterruptedException e) {
            log.warn("Failed to take a message: {}", e.getMessage());
        }
    }

    public void takeHeating() {
        try {
            OnHeatingEvent heatingEvent = heatingQueue.take();
            log.info("Took heating event {} with delay {}", heatingEvent.isPumpStatus(), heatingEvent.getStartTime() );
            heatingService.pump(heatingEvent.isPumpStatus());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
