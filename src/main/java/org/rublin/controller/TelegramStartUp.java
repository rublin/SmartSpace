package org.rublin.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.BotSession;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 15.09.2016.
 */
public class TelegramStartUp  {

    public static final Logger LOG = getLogger(TelegramStartUp.class);
    private BotSession session;

    @Autowired
    private TelegramController telegramController;

    public void startTelegramBotApi() {
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            session =
                    api.registerBot(telegramController);
            LOG.info("Telegram BOT started!");
        } catch (TelegramApiException e) {
            LOG.error("Failed to register bot {} due to error ", telegramController.getBotUsername(), e.getMessage());
        }
    }
    public void stopTelegramApi() {
        session.close();
    }
}
