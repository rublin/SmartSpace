package org.rublin.util;

import jersey.repackaged.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 28.08.2016.
 */
public class Telegram extends TelegramLongPollingCommandBot {

    private static final Logger LOG = getLogger(Notification.class);

    public static Set<String> users = new HashSet<>();

    public static final String BOT_USERNAME = Notification.readProperties().getProperty("telegram.bot.username");
    public static final String BOT_TOKEN = Notification.readProperties().getProperty("telegram.bot.token");

    {
        users.addAll(Arrays.asList("Helenko", "rublinua"));
    }
    @Override
    public void processNonCommandUpdate(Update update) {
        //check if the update has a message
        if(update.hasMessage()) {
            Message message = update.getMessage();
            User user = message.getFrom();
            System.out.println();
            if (users.contains(user.getUserName())){
                if(message.hasText()){
                    if (message.getText().equals("/start")) {
                        String uniqueCode = message.getText().split(" ").length > 0 ? message.getText().split(" ")[1] : null;
                        System.out.println(uniqueCode);
                        if (uniqueCode != null) {
//                            String user = users.containsKey(uniqueCode) ? users.get(uniqueCode) : "Nobody";

                        }
                    }
                    if(message.getText().equals("/cam")){
                        SendPhoto sendPhotoRequest = new SendPhoto();
                        sendPhotoRequest.setChatId(message.getChatId().toString());
                        //path: String,                     photoName: String
//                        sendPhotoRequest.setNewPhoto("28-08-2016-01-16.jpg", "CamIn01.jpg");
                        sendPhotoRequest.setNewPhoto(Notification.getImageFromCamera("http://192.168.0.31/Streaming/channels/1/picture"), "CamIn01.jpg");
                        System.out.println("cam1");
                        try {
                            sendPhoto(sendPhotoRequest);
                        } catch (TelegramApiException e) {
                            //do some error handling
                            LOG.error(e.getMessage());
                        }
                    }
                }
            } else {
                SendMessage sendMessageRequest = new SendMessage();
                sendMessageRequest.setChatId(message.getChatId().toString());
                sendMessageRequest.setText(String.format("Hello %s, how are you today? You are not authorized to this bot, sorry.", user.getFirstName()));
                try {
                    sendMessage(sendMessageRequest);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            //check if the message has text. it could also contain for example a location ( message.hasLocation() )

        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    public static void main(String[] args) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Telegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
