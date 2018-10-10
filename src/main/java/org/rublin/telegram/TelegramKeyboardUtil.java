package org.rublin.telegram;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.rublin.telegram.TelegramCommand.*;

public class TelegramKeyboardUtil {
    public static ReplyKeyboardMarkup mainKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();

        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(createKeyboardRow(SECURITY.getCommandName(), CAMERA.getCommandName()));
        keyboard.add(createKeyboardRow(WEATHER.getCommandName(), MEDIA.getCommandName()));
        keyboard.add(createKeyboardRow(INFO.getCommandName()));

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup securityKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(createKeyboardRow(ARMING.getCommandName(), DISARMING.getCommandName()));
        keyboard.add(createKeyboardRow(MAIN.getCommandName()));

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup armingOrDisarmingKeyboard(List<String> zones) {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(createKeyboardRow(zones.toArray(new String[0])));
        keyboard.add(createKeyboardRow(SECURITY_ALL.getCommandName()));
        keyboard.add(createKeyboardRow(MAIN.getCommandName()));

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup mediaKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(createKeyboardRow(RADIO.getCommandName(), SAY.getCommandName()));
        keyboard.add(createKeyboardRow(VOLUME.getCommandName()));
        keyboard.add(createKeyboardRow(MAIN.getCommandName()));

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup weatherKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(createKeyboardRow(CONDITION.getCommandName(), FORECAST.getCommandName()));
        keyboard.add(createKeyboardRow(MAIN.getCommandName()));

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup cameraKeyboard(List<String> cameras) {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(createKeyboardRow(cameras.toArray(new String[0])));
        keyboard.add(createKeyboardRow(CAMERA_ALL.getCommandName()));
        keyboard.add(createKeyboardRow(MAIN.getCommandName()));

        return keyboardMarkup;
    }

    private static ReplyKeyboardMarkup initKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(new ArrayList<>());
        return keyboardMarkup;
    }

    private static KeyboardRow createKeyboardRow(String... commands) {
        KeyboardRow row = new KeyboardRow();
        Arrays.stream(commands)
                .forEach(row::add);
        return row;
    }
}
