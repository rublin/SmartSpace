package org.rublin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.model.Trigger;
import org.rublin.model.Zone;
import org.rublin.model.event.Event;
import org.rublin.service.*;
import org.rublin.util.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;


/**
 * Created by Sheremet on 28.08.2016.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class TelegramController extends TelegramLongPollingBot {
    
    private static Set<Integer> telegramIds = new HashSet<>();
    private static Set<Long> chatIds = new HashSet<>();

    private final ZoneService zoneService;
    private final UserService userService;
    private final CameraService cameraService;
    private final WeatherService weatherService;
    private final TextToSpeechService textToSpeechService;
    private final MediaPlayerService mediaPlayerService;
    private final TriggerService triggerService;
    private final EventService eventService;

    @Value("${radio}")
    private String onlineRadio;

    @Value("${telegram.bot.username}")
    private String username;
    
    @Value("${telegram.bot.token}")
    private String token;

    @Value("${weather.city}")
    private String city;

    @Value("${weather.lang}")
    private String lang;

    @Value("${tmp.directory}")
    private String tmpDir;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (isAuthorize(message.getFrom())) {
                chatIds.add(message.getChatId());
                log.info(message.getText().substring(0, 3).toLowerCase());
                switch (message.getText().substring(0, 3).toLowerCase()) {
                    case "/aa": {
                        Collection<Zone> zones = zoneService.getAll();
                        for (Zone zone : zones) {
                            zoneService.setSecure(zone, true);
                            sendTextMessage(message.getChatId().toString(), format(
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
                            sendTextMessage(message.getChatId().toString(), format(
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
                                sendTextMessage(message.getChatId().toString(), format(
                                        "Zone <b>%s</b> changed security to <b>%s</b>",
                                        zone.getName(),
                                        zone.isSecure()));
                            } catch (Exception e) {
                                log.error("Error to change state for zone {} to {}", command[1], command[2]);
                                sendTextMessage(message.getChatId().toString(), "Can't find zone with id: " + command[1]);
                            }
                        } else {
                            sendTextMessage(message.getChatId().toString(), format(
                                    "Command %s is not support. " +
                                            "Use format like <b>/ss id 1</b> to arming, " +
                                            "and <b>/ss id 0</b> to disarming. Do not forget to change id to current zone id!",
                                    message.getText()));
                        }
                        break;
                    case "/ca": {
                        cameraService.getAll().forEach(
                                camera -> sendPhotoMessage(message.getChatId().toString(),
                                        Image.getImageFromCamera(camera, tmpDir)));
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
                        String forecast = weatherService.getForecast(city, lang);
                        sendTextMessage(message.getChatId().toString(), forecast);
                        textToSpeechService.say(forecast, "uk");
                        break;
                    }
                    case "/wc": {
                        String condition = weatherService.getCondition(city, lang);
                        sendTextMessage(message.getChatId().toString(), condition);
                        textToSpeechService.say(condition, "uk");
                        break;
                    }
                    case "/pl": {
                        mediaPlayerService.play(onlineRadio);
                        break;
                    }

                    case "/st": {
                        mediaPlayerService.stop();
                        break;
                    }

                    case "/sa": {
                        String[] split = message.getText().split("\n");
                        if (split.length > 1)
                            textToSpeechService.say(split[2].trim(), split[1].trim());
                        else if (split.length == 1)
                            textToSpeechService.say(split[1], "uk");
                        break;
                    }

                    case "/ts": {
                        if (message.getText().matches("\\/ts\\s\\d+")) {
                            String[] split = message.getText().split(" ");
                            try {
                                Trigger trigger = triggerService.get(Integer.valueOf(split[1]));
                                List<Event> events = eventService.get(trigger, 5);
                                sendTextMessage(message.getChatId().toString(), format("Found last 5 events for %s trigger:", trigger.getName()));
                                events.forEach(e -> sendTextMessage(message.getChatId().toString(), e));
                            } catch (Throwable throwable) {
                                log.info("", throwable);
                                sendTextMessage(message.getChatId().toString(), throwable.getMessage());
                            }
                        } else {
                            triggerService.getAll().forEach(t -> sendTextMessage(message.getChatId().toString(), t));
                        }
                        break;
                    }

                    case "/tr": {
                        if (message.getText().matches("\\/tr\\s\\d+\\s[0-1]")) {
                            String[] split = message.getText().split(" ");
                            try {
                                Trigger trigger = triggerService.get(Integer.valueOf(split[1]));
                                trigger.setActive(Boolean.valueOf(split[2]));
                                triggerService.save(trigger, trigger.getZone());
                                String logMessage = format("Set active to %s for trigger %s", trigger.isActive(), trigger.getName());
                                log.info(logMessage);
                                sendTextMessage(message.getChatId().toString(), logMessage);
                            } catch (Throwable throwable) {
                                log.info("", throwable);
                                sendTextMessage(message.getChatId().toString(), throwable.getMessage());
                            }
                        } else {
                            sendTextMessage(message.getChatId().toString(), format(
                                    "Command %s is not support. " +
                                            "Use format like <b>/tr id 1</b> to activate, " +
                                            "and <b>/ss id 0</b> to disactivate. Do not forget to change id to current trigger id!",
                                    message.getText()));
                        }
                        break;
                    }

                    default:
                        sendTextMessage(message.getChatId().toString(), format("Your command does not support. Try to use /help"));
                }
            } else {
                sendTextMessage(message.getChatId().toString(), format(
                        "Hello <b>%s</b>, how are you today?" +
                                "<i>You are not authorized to use this bot, sorry.</i>",
                        message.getFrom().getFirstName()));
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onClosing() {

    }

    private void sendTextMessage(String id, Trigger trigger) {
        String html = format("ID: <b>%d</b>; name: <b>%s</b>; type: <b>%s</b>;", trigger.getId(), trigger.getName(), trigger.getType().name());
        sendTextMessage(id, html);
    }

    private void sendTextMessage(String id, Event event) {
        String html = format("Time: <b>%s</b>; name: <b>%s</b>; state: <b>%s</b>", event.getTime(), event.getTrigger().getName(), event.getState());
        sendTextMessage(id, html);
    }

    private void sendTextMessage(String id, String html) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(id);
        sendMessageRequest.setText(html);
        sendMessageRequest.enableHtml(true);
        try {
            log.info(sendMessageRequest.getText());
            execute(sendMessageRequest);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendPhotoMessage(String id, File file) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(id);
//        sendPhotoRequest.setNewPhoto(EmailController.getImageFromCamera("http://192.168.0.31/Streaming/channels/1/picture"), "CamIn01.jpg");
        sendPhotoRequest.setNewPhoto(file);
        log.info("Sending photo: {}", sendPhotoRequest.toString());
        try {
            sendPhoto(sendPhotoRequest);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isAuthorize(User user) {
        String telegramName = user.getUserName();
        int telegramId = user.getId();
        log.debug("Received message from user {} (id: {})", telegramName, telegramId);
        if (telegramIds.contains(telegramId)) {
            org.rublin.model.user.User foundUser = userService.getByTelegramId(telegramId);
            log.info("Telegram User {} with id {} authorized as {}. Quick authorization", telegramName, telegramId, foundUser.getFirstName());
            if (!foundUser.getTelegramName().equals(telegramName)) {
                foundUser.setTelegramName(telegramName);
                log.info("Telegram name {} updated", telegramName);
            }
            return true;
        } else {
            List<org.rublin.model.user.User> users = userService.getAll();
            for (org.rublin.model.user.User u : users) {
                String foundTelegramName = u.getTelegramName();
//                log.debug("Compare name");
                if (foundTelegramName != null && foundTelegramName.equals(telegramName)) {
                    log.info("Telegram User {} with id {} authorized as {}. Long authorization", telegramName, telegramId, u.getFirstName());
                    u.setTelegramId(telegramId);
                    telegramIds.add(telegramId);
                    userService.update(u);
                    return true;
                }
            }
        }
        log.info("Telegram User {} with id {} not authorized.", telegramName, telegramId);
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
}
