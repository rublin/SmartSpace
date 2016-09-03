package org.rublin.controller;

import org.rublin.model.Zone;
import org.rublin.service.ZoneService;
import org.rublin.util.Notification;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

import java.io.File;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 28.08.2016.
 */
@Controller
public class Telegram extends TelegramLongPollingCommandBot {

    private static final Logger LOG = getLogger(Notification.class);

    public static Set<String> users = new HashSet<>();

    private static final String BOT_USERNAME = Notification.readProperties().getProperty("telegram.bot.username");
    private static final String BOT_TOKEN = Notification.readProperties().getProperty("telegram.bot.token");

    @Autowired
    private ZoneService zoneService;

    {
        users.addAll(Arrays.asList("Helenko", "rublinua"));
    }
    private void sendTextMessage(String id, String html) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(id);
        sendMessageRequest.setText(html);
        sendMessageRequest.enableHtml(true);
        try {
            LOG.info(sendMessageRequest.getText());
            sendMessage(sendMessageRequest);
        } catch (TelegramApiException e) {
            LOG.error(e.getMessage());
        }
    }
    private void sendPhotoMessage(String id, File file) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(id);
//        sendPhotoRequest.setNewPhoto(Notification.getImageFromCamera("http://192.168.0.31/Streaming/channels/1/picture"), "CamIn01.jpg");
        sendPhotoRequest.setNewPhoto(file);
        LOG.info("Sending photo: {}", sendPhotoRequest.toString());
        try {
            sendPhoto(sendPhotoRequest);
        } catch (TelegramApiException e) {
            LOG.error(e.getMessage());
        }
    }
    private boolean isAuthorize(User user) {
        LOG.info("Received message from user {} (id: {})", user.getUserName(), user.getId());
        return users.contains(user.getUserName());
    }
    private String zoneInfo(Zone zone) {
        return String.format(
                "id: <b>%d</b>, name: <b>%s</b>, status: <b>%s</b>, secure: <b>%s</b>",
                zone.getId(),
                zone.getName(),
                zone.getStatus().toString(),
                zone.getSecure() ? "YES" : "NO");
    }
    @Override
    public void processNonCommandUpdate(Update update) {
        if(update.hasMessage()) {
            Message message = update.getMessage();
            if (isAuthorize(message.getFrom())){
                LOG.info(message.getText().substring(0,3).toLowerCase());
                switch (message.getText().substring(0,3).toLowerCase()) {
                    case "/aa": {
                        Collection<Zone> zones = zoneService.getAll();
                        for (Zone zone : zones) {
                            zone.setSecure(true);
                            sendTextMessage(message.getChatId().toString(), String.format(
                                    "Zone <b>%s</b> is <b>arming</b> now",
                                    zone.getName()));
                            sendTextMessage(message.getChatId().toString(), zoneInfo(zone));
                        }
                        Notification.sendMail("Telegram security change", String.format("<h2>All zones are arming</h2>\n" +
                                        "<h3>by user %s</h3>",
                                message.getFrom().getFirstName()));
                        break;
                    }
                    case "/da" : {
                        Collection<Zone> zones = zoneService.getAll();
                        for (Zone zone : zones) {
                            zone.setSecure(false);
                            sendTextMessage(message.getChatId().toString(), String.format(
                                    "Zone <b>%s</b> is <b>disarming</b> now",
                                    zone.getName()));
                            sendTextMessage(message.getChatId().toString(), zoneInfo(zone));
                        }
                        Notification.sendMail("Telegram security change", String.format("<h2>All zones are disarming</h2>\n" +
                                        "<h3>by user %s</h3>",
                                message.getFrom().getFirstName()));
                        break;
                    }
                    case "/ss" :
                        if (message.getText().matches("\\/ss\\s\\d+\\s[0-1]")) {
                            String[] command = message.getText().split(" ");
                            try {
                                Zone zone = zoneService.get(Integer.parseInt(command[1]));
                                zone.setSecure(command[2].equals("0") ? Boolean.FALSE : Boolean.TRUE);
                                zoneService.save(zone);
                                sendTextMessage(message.getChatId().toString(), String.format(
                                        "Zone <b>%s</b> changed security to <b>%s</b>",
                                        zone.getName(),
                                        zone.getSecure()));
                                Notification.sendMail("Telegram security change", String.format("<h1>Zone: <span style=\"color: blue;\">%s</span></h1>\n" +
                                                "<h2>Status: <span style=\"color: %s;\">%s</span></h2>\n" +
                                                "<h2>Secure: <span style=\"color: %s;\">%s</span></h2>\n" +
                                                "<h3>by user %s</h3>",
                                        zone.getName(),
                                        zone.getStatus(),
                                        zone.getStatus(),
                                        zone.getSecure() ? "GREEN" : "GREY",
                                        zone.getSecure(),
                                        message.getFrom().getFirstName()));
                            } catch (Exception e) {
                                LOG.error("Error to change state for zone {} to {}", command[1], command[2]);
                                sendTextMessage(message.getChatId().toString(), "Can't find zone with id: " + command[1]);
                            }
                        } else {
                            sendTextMessage(message.getChatId().toString(), String.format(
                                    "Command %s is not support. " +
                                            "Use format like <b>/ss id 1</b> to arming, " +
                                            "and <b>/ss id 0</b> to disarming. Do not forget to change id to current zone id!",
                                    message.getText()));
                        }
                        break;
                    case "/ca" : sendPhotoMessage(message.getChatId().toString(), new File(Notification.getImageFromCamera("http://192.168.0.31/Streaming/channels/1/picture")));
                        break;
                    case "/gs" : {
                        Collection<Zone> zones = zoneService.getAll();
                        for (Zone zone : zones) {
                            sendTextMessage(message.getChatId().toString(), zoneInfo(zone));
                        }
                        break;
                    }
                }
            } else {
                sendTextMessage(message.getChatId().toString(), String.format(
                        "Hello <b>%s</b>, how are you today?" +
                                "<i>You are not authorized to use this bot, sorry.</i>",
                        message.getFrom().getFirstName()));
            }
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

    /*public static void main(String[] args) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Telegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }*/
}
