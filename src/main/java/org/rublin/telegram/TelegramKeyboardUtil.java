package org.rublin.telegram;

import org.rublin.model.user.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.rublin.telegram.TelegramCommand.*;

public class TelegramKeyboardUtil {
    static ReplyKeyboardMarkup mainKeyboard(User user) {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();

        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.remove(0);
        keyboard.add(createKeyboardRow(SECURITY.getCommandName(), CAMERA.getCommandName()));
        keyboard.add(createKeyboardRow(WEATHER.getCommandName(), MEDIA.getCommandName(), EVENTS.getCommandName()));
        if (user.isAdmin())
            keyboard.add(createKeyboardRow(ADMIN.getCommandName()));
        keyboard.add(createKeyboardRow(INFO.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup adminKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(ADMIN_TRIGGERS.getCommandName(), ADMIN_ZONES.getCommandName(), ADMIN_CAMERAS.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup zonesKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(createKeyboardRow(ADMIN_ZONE_ADD.getCommandName(), ADMIN_ZONE_EDIT.getCommandName(), ADMIN_ZONE_REMOVE.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup triggerKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        keyboardMarkup.getKeyboard().add(0, createKeyboardRow(ADMIN_TRIGGER_ADD.getCommandName(), ADMIN_TRIGGER_EDIT.getCommandName(), ADMIN_TRIGGER_REMOVE.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup cameraKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        keyboardMarkup.getKeyboard().add(0, createKeyboardRow(ADMIN_CAMERA_ADD.getCommandName(), ADMIN_CAMERA_EDIT.getCommandName(), ADMIN_TRIGGER_REMOVE.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup securityKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(ARMING.getCommandName(), DISARMING.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup eventsKeyboard(List<String> triggers) {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(triggers.toArray(new String[0])));

        return keyboardMarkup;
    }
    static ReplyKeyboardMarkup armingOrDisarmingKeyboard(List<String> zones) {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(zones.toArray(new String[0])));
        keyboard.add(1, createKeyboardRow(SECURITY_ALL.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup mediaKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(RADIO.getCommandName(), SAY.getCommandName()));
        keyboard.add(1, createKeyboardRow(VOLUME.getCommandName(), STOP.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup sayKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(LANGUAGE_UK.getCommandName(), LANGUAGE_EN.getCommandName(), LANGUAGE_DE.getCommandName()));
        keyboard.add(1, createKeyboardRow(LANGUAGE_OTHER.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup volumeKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        keyboardMarkup.getKeyboard().add(0, createKeyboardRow(VOLUME_UP.getCommandName(), VOLUME_DOWN.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup weatherKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(CONDITION.getCommandName(), FORECAST.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup cameraKeyboard(List<String> cameras) {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(cameras.toArray(new String[0])));
        keyboard.add(1, createKeyboardRow(CAMERA_ALL.getCommandName()));

        return keyboardMarkup;
    }

    private static ReplyKeyboardMarkup initKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(new ArrayList<>());
        keyboardMarkup.getKeyboard().add(createKeyboardRow(MAIN.getCommandName()));

        return keyboardMarkup;
    }

    private static KeyboardRow createKeyboardRow(String... commands) {
        KeyboardRow row = new KeyboardRow();
        Arrays.stream(commands)
                .forEach(row::add);
        return row;
    }
}
