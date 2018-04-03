package org.rublin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

/**
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramStartUp {

    private final TelegramController telegramController;

    private TelegramBotsApi telegramBotsApi;


    @PostConstruct
    private void init() {

        telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(telegramController);
        } catch (TelegramApiRequestException e) {
            log.error("Failed to register bot: {}", e);
        }
    }
}
