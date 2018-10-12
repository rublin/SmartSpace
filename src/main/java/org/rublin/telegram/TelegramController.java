package org.rublin.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.to.TelegramResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.Objects;


/**
 * Created by Sheremet on 28.08.2016.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramController extends TelegramLongPollingBot {

    private final TelegramService telegramService;


    @Value("${telegram.bot.username}")
    private String username;
    
    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            sendResponseMessage(telegramService.process(update.getMessage()));
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

    public void sendAlarmMessage(String message) {
        telegramService.getChatIds().forEach(id -> sendTextMessage(id.toString(), message, null));
    }

    public void sendAlarmMessage(List<File> photos) {
        photos.forEach(photo ->
                telegramService.getChatIds().forEach(id ->
                        sendPhotoMessage(id.toString(), photo, null)
                ));

    }
}
