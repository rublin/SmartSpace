package org.rublin.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.model.Trigger;
import org.rublin.model.event.Event;
import org.rublin.service.UserService;
import org.rublin.to.TelegramResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;
import static org.rublin.telegram.TelegramKeyboardUtil.mainKeyboard;


/**
 * Created by Sheremet on 28.08.2016.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramController extends TelegramLongPollingBot {
    
    private static Set<Integer> telegramIds = new HashSet<>();
    private static Set<Long> chatIds = new HashSet<>();

    private final UserService userService;
    private final TelegramService telegramService;


    @Value("${telegram.bot.username}")
    private String username;
    
    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (isAuthorize(message.getFrom())) {
                chatIds.add(message.getChatId());
                sendResponseMessage(telegramService.process(message));
            } else {
                sendTextMessage(message.getChatId().toString(), format(
                        "Hello <b>%s</b>, how are you today?" +
                                "<i>You are not authorized to use this bot, sorry.</i>",
                        message.getFrom().getFirstName()), mainKeyboard());
            }
        }
    }

    private void sendResponseMessage(TelegramResponseDto response) {
        if (Objects.nonNull(response.getMessages())) {
            response.getMessages().forEach(message -> sendTextMessage(response.getId(), message, response.getKeyboard()));
        }

        if (Objects.nonNull(response.getFiles())) {
            response.getFiles().forEach(file -> sendPhotoMessage(response.getId(), file, response.getKeyboard()));
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

    @Deprecated
    private void sendTextMessage(String id, Trigger trigger) {
        String html = format("ID: <b>%d</b>; name: <b>%s</b>; type: <b>%s</b>;", trigger.getId(), trigger.getName(), trigger.getType().name());
        sendTextMessage(id, html, mainKeyboard());
    }

    @Deprecated
    private void sendTextMessage(String id, Event event) {
        String html = format("Time: <b>%s</b>; name: <b>%s</b>; state: <b>%s</b>", event.getTime(), event.getTrigger().getName(), event.getState());
        sendTextMessage(id, html, mainKeyboard());
    }

    public void sendTextMessage(String id, String html, ReplyKeyboardMarkup keyboard) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(id);
        sendMessageRequest.setText(html);
        sendMessageRequest.enableHtml(true);
        sendMessageRequest.setReplyMarkup(keyboard);
        try {
            log.info(sendMessageRequest.getText());
            execute(sendMessageRequest);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendPhotoMessage(String id, File file, ReplyKeyboardMarkup keyboard) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(id);
//        sendPhotoRequest.setNewPhoto(EmailController.getImageFromCamera("http://192.168.0.31/Streaming/channels/1/picture"), "CamIn01.jpg");
        sendPhotoRequest.setNewPhoto(file);
        sendPhotoRequest.setReplyMarkup(keyboard);
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
        chatIds.forEach(id -> sendTextMessage(id.toString(), message, mainKeyboard()));
    }

    public void sendAlarmMessage(List<File> photos) {
        photos.forEach(photo ->
                chatIds.forEach(id ->
                        sendPhotoMessage(id.toString(), photo, mainKeyboard())
                ));

    }
}
