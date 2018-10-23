package org.rublin.service.delayed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.controller.NotificationService;
import org.rublin.message.NotificationMessage;
import org.rublin.model.ConfigKey;
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
    private final NotificationService notificationService;
    private final MediaPlayerService mediaPlayerService;
    private final SystemConfigService configService;
    private final TextToSpeechService textToSpeechService;


    public void put(NotificationMessage message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            log.warn("Failed to put a message: {}", e.getMessage());
        }
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
}
