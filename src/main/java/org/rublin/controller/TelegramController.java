package org.rublin.controller;

import org.rublin.model.Zone;
import org.rublin.service.CameraService;
import org.rublin.service.UserService;
import org.rublin.service.ZoneService;
import org.rublin.util.Image;
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
import static org.rublin.util.Resources.*;


/**
 * Created by Sheremet on 28.08.2016.
 */
@Controller
public class TelegramController extends TelegramLongPollingCommandBot {

    private static final Logger LOG = getLogger(TelegramController.class);

    public static Set<Integer> telegramIds = new HashSet<>();
    private static Set<Long> chatIds = new HashSet<>();

    private static final String BOT_USERNAME = TELEGRAM_BOT_NAME;
    private static final String BOT_TOKEN = TELEGRAM_TOKEN;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private UserService userService;

    @Autowired
    private CameraService cameraService;

    @Autowired
    private WeatherController weatherController;

    @Autowired
    private TTSController ttsController;

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
//        sendPhotoRequest.setNewPhoto(EmailController.getImageFromCamera("http://192.168.0.31/Streaming/channels/1/picture"), "CamIn01.jpg");
        sendPhotoRequest.setNewPhoto(file);
        LOG.info("Sending photo: {}", sendPhotoRequest.toString());
        try {
            sendPhoto(sendPhotoRequest);
        } catch (TelegramApiException e) {
            LOG.error(e.getMessage());
        }
    }

    private boolean isAuthorize(User user) {
        String telegramName = user.getUserName();
        int telegramId = user.getId();
        LOG.debug("Received message from user {} (id: {})", telegramName, telegramId);
        if (telegramIds.contains(telegramId)) {
            org.rublin.model.user.User foundUser = userService.getByTelegramId(telegramId);
            LOG.info("Telegram User {} with id {} authorized as {}. Quick authorization", telegramName, telegramId, foundUser.getFirstName());
            if (!foundUser.getTelegramName().equals(telegramName)) {
                foundUser.setTelegramName(telegramName);
                LOG.info("Telegram name {} updated", telegramName);
            }
            return true;
        } else {
            List<org.rublin.model.user.User> users = userService.getAll();
            for (org.rublin.model.user.User u : users) {
                String foundTelegramName = u.getTelegramName();
//                LOG.debug("Compare name");
                if (foundTelegramName != null && foundTelegramName.equals(telegramName)) {
                    LOG.info("Telegram User {} with id {} authorized as {}. Long authorization", telegramName, telegramId, u.getFirstName());
                    u.setTelegramId(telegramId);
                    telegramIds.add(telegramId);
                    userService.update(u);
                    return true;
                }
            }
        }
        LOG.info("Telegram User {} with id {} not authorized.", telegramName, telegramId);
        return false;
    }

    public void sendAlarmMessage(String message) {
        chatIds.forEach(id -> sendTextMessage(id.toString(), message));
    }

    public void sendAlarmMessage(List<File> photos) {
        photos.forEach(photo ->
                chatIds.forEach(id ->
                        sendPhotoMessage(id.toString(), photo)
                ));

    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (isAuthorize(message.getFrom())) {
                chatIds.add(message.getChatId());
                LOG.info(message.getText().substring(0, 3).toLowerCase());
                switch (message.getText().substring(0, 3).toLowerCase()) {
                    case "/aa": {
                        Collection<Zone> zones = zoneService.getAll();
                        for (Zone zone : zones) {
                            zoneService.setSecure(zone, true);
                            sendTextMessage(message.getChatId().toString(), String.format(
                                    "Zone <b>%s</b> is <b>arming</b> now",
                                    zone.getName()));
                            sendTextMessage(message.getChatId().toString(), zoneService.getInfo(zone));
                        }
                        break;
                    }
                    case "/da": {
                        Collection<Zone> zones = zoneService.getAll();
                        for (Zone zone : zones) {
                            zoneService.setSecure(zone, false);
                            sendTextMessage(message.getChatId().toString(), String.format(
                                    "Zone <b>%s</b> is <b>disarming</b> now",
                                    zone.getName()));
                            sendTextMessage(message.getChatId().toString(), zoneService.getInfo(zone));
                        }
                        break;
                    }
                    case "/ss":
                        if (message.getText().matches("\\/ss\\s\\d+\\s[0-1]")) {
                            String[] command = message.getText().split(" ");
                            try {
                                Zone zone = zoneService.get(Integer.parseInt(command[1]));
                                zoneService.setSecure(zone, command[2].equals("0") ? Boolean.FALSE : Boolean.TRUE);
                                sendTextMessage(message.getChatId().toString(), String.format(
                                        "Zone <b>%s</b> changed security to <b>%s</b>",
                                        zone.getName(),
                                        zone.isSecure()));
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
                    case "/ca": {
                        cameraService.getAll().forEach(camera -> sendPhotoMessage(message.getChatId().toString(), Image.getImageFromCamera(camera)));
                    }
                    break;
                    case "/gs": {
                        Collection<Zone> zones = zoneService.getAll();
                        for (Zone zone : zones) {
                            sendTextMessage(message.getChatId().toString(), zoneService.getInfo(zone));
                        }
                        break;

                    }
                    case "/wf": {
                        String forecast = weatherController.getForecast(WEATHER_CITY, WEATHER_LANG);
                        sendTextMessage(message.getChatId().toString(), forecast);
                        ttsController.say(forecast, "uk");
                        break;
                    }
                    case "/wc" : {
                        String condition = weatherController.getCondition(WEATHER_CITY, WEATHER_LANG);
                        sendTextMessage(message.getChatId().toString(), condition);
                        ttsController.say(condition, "uk");
                        break;
                    }
                    default:
                        sendTextMessage(message.getChatId().toString(), String.format("Your command does not support. Try to use /help"));
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
}
