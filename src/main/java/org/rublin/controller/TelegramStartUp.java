package org.rublin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.service.CameraService;
import org.rublin.service.UserService;
import org.rublin.service.ZoneService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramStartUp {



    public static final Logger LOG = getLogger(TelegramStartUp.class);
    //    private BotSession session;
    private final ZoneService zoneService;
    private final UserService userService;
    private final CameraService cameraService;
    private final WeatherController weatherController;
    private final TTSController ttsController;
    private final TelegramController telegramController;

    private TelegramBotsApi telegramBotsApi;


    @PostConstruct
    private void init() {

        telegramBotsApi = new TelegramBotsApi();
//        telegramController = new TelegramController(zoneService, userService, cameraService, weatherController, ttsController);

        try {
            telegramBotsApi.registerBot(telegramController);
        } catch (TelegramApiRequestException e) {
            log.error("Failed to register bot: {}", e);
        }
    }
}
