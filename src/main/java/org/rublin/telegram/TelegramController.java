package org.rublin.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rublin.to.TelegramResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
            typing(update.getMessage().getChatId());
            sendResponseMessage(telegramService.process(update.getMessage()));
        }
    }

    @SneakyThrows
    private void typing(Long chatId) {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chatId);
        sendChatAction.setAction(ActionType.TYPING);

        execute(sendChatAction);
    }

    private void sendResponseMessage(TelegramResponseDto response) {
        if (Objects.nonNull(response.getMessages())) {
            response.getMessages().forEach(message -> sendTextMessage(response.getId(), message, response.getKeyboard()));
        }

        if (Objects.nonNull(response.getFiles()) && !response.getFiles().isEmpty()) {
            sendTextMessage(response.getId(), "Here are your photos...", response.getKeyboard());
            sendPhotoMessage(response.getId(), response.getFiles());
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

    @SneakyThrows
    private void sendPhotoMessage(String id, List<File> files) {
        List<InputMedia> mediaPhotos = files.stream()
                .filter(Objects::nonNull)
                .map(file -> {
                    InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
                    inputMediaPhoto.setMedia(file, file.getName());
                    return inputMediaPhoto;
                })
                .collect(Collectors.toList());

        SendMediaGroup group = new SendMediaGroup();
        group.setChatId(id);
        group.setMedia(mediaPhotos);

        execute(group);
    }

    private void sendPhotoMessage(String id, File file, ReplyKeyboardMarkup keyboard) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(id);
//        sendPhotoRequest.setNewPhoto(EmailController.getImageFromCamera("http://192.168.0.31/Streaming/channels/1/picture"), "CamIn01.jpg");
        sendPhotoRequest.setPhoto(file);
        sendPhotoRequest.setReplyMarkup(keyboard);
        log.info("Sending photo: {}", sendPhotoRequest.toString());
        try {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void sendAlarmMessage(String message) {
        telegramService.getChatIds().forEach(id -> sendTextMessage(id.toString(), message, null));
    }

    public void sendAlarmMessage(List<File> photos) {
        telegramService.getChatIds().forEach(id -> sendPhotoMessage(id.toString(), photos));
    }
}
