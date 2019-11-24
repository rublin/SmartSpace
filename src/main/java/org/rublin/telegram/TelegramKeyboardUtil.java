package org.rublin.telegram;

import org.rublin.model.user.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.rublin.telegram.TelegramCommand.ADMIN;
import static org.rublin.telegram.TelegramCommand.ADMIN_CAMERAS;
import static org.rublin.telegram.TelegramCommand.ADMIN_CAMERA_ADD;
import static org.rublin.telegram.TelegramCommand.ADMIN_CAMERA_EDIT;
import static org.rublin.telegram.TelegramCommand.ADMIN_SENSORS;
import static org.rublin.telegram.TelegramCommand.ADMIN_SENSOR_ADD;
import static org.rublin.telegram.TelegramCommand.ADMIN_SENSOR_EDIT;
import static org.rublin.telegram.TelegramCommand.ADMIN_SENSOR_REMOVE;
import static org.rublin.telegram.TelegramCommand.ADMIN_TEMPERATURE_SENSOR_ADD;
import static org.rublin.telegram.TelegramCommand.ADMIN_ZONES;
import static org.rublin.telegram.TelegramCommand.ADMIN_ZONE_ADD;
import static org.rublin.telegram.TelegramCommand.ADMIN_ZONE_EDIT;
import static org.rublin.telegram.TelegramCommand.ADMIN_ZONE_REMOVE;
import static org.rublin.telegram.TelegramCommand.ARMING;
import static org.rublin.telegram.TelegramCommand.CAMERA;
import static org.rublin.telegram.TelegramCommand.CAMERA_ALL;
import static org.rublin.telegram.TelegramCommand.CONDITION;
import static org.rublin.telegram.TelegramCommand.DISARMING;
import static org.rublin.telegram.TelegramCommand.EVENTS;
import static org.rublin.telegram.TelegramCommand.FORECAST;
import static org.rublin.telegram.TelegramCommand.INFO;
import static org.rublin.telegram.TelegramCommand.LANGUAGE_DE;
import static org.rublin.telegram.TelegramCommand.LANGUAGE_EN;
import static org.rublin.telegram.TelegramCommand.LANGUAGE_OTHER;
import static org.rublin.telegram.TelegramCommand.LANGUAGE_UK;
import static org.rublin.telegram.TelegramCommand.MAIN;
import static org.rublin.telegram.TelegramCommand.MEDIA;
import static org.rublin.telegram.TelegramCommand.RADIO;
import static org.rublin.telegram.TelegramCommand.RELAY;
import static org.rublin.telegram.TelegramCommand.RELAY_0;
import static org.rublin.telegram.TelegramCommand.RELAY_10;
import static org.rublin.telegram.TelegramCommand.RELAY_100;
import static org.rublin.telegram.TelegramCommand.RELAY_20;
import static org.rublin.telegram.TelegramCommand.RELAY_30;
import static org.rublin.telegram.TelegramCommand.RELAY_40;
import static org.rublin.telegram.TelegramCommand.RELAY_50;
import static org.rublin.telegram.TelegramCommand.RELAY_60;
import static org.rublin.telegram.TelegramCommand.RELAY_70;
import static org.rublin.telegram.TelegramCommand.RELAY_80;
import static org.rublin.telegram.TelegramCommand.RELAY_90;
import static org.rublin.telegram.TelegramCommand.SAY;
import static org.rublin.telegram.TelegramCommand.SECURITY;
import static org.rublin.telegram.TelegramCommand.SECURITY_ALL;
import static org.rublin.telegram.TelegramCommand.STOP;
import static org.rublin.telegram.TelegramCommand.VOLUME;
import static org.rublin.telegram.TelegramCommand.VOLUME_DOWN;
import static org.rublin.telegram.TelegramCommand.VOLUME_UP;
import static org.rublin.telegram.TelegramCommand.WEATHER;

public class TelegramKeyboardUtil {
    public static ReplyKeyboardMarkup mainKeyboard(User user) {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();

        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.remove(0);
        keyboard.add(createKeyboardRow(SECURITY.getCommandName(), RELAY.getCommandName(), CAMERA.getCommandName()));
        keyboard.add(createKeyboardRow(WEATHER.getCommandName(), MEDIA.getCommandName(), EVENTS.getCommandName()));
        if (user.isAdmin())
            keyboard.add(createKeyboardRow(ADMIN.getCommandName(), INFO.getCommandName()));
        else
            keyboard.add(createKeyboardRow(INFO.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup adminKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(ADMIN_TEMPERATURE_SENSOR_ADD.getCommandName()));
        keyboard.add(0, createKeyboardRow(ADMIN_SENSORS.getCommandName(), ADMIN_ZONES.getCommandName(), ADMIN_CAMERAS.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup zonesKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(ADMIN_ZONE_ADD.getCommandName(), ADMIN_ZONE_EDIT.getCommandName(), ADMIN_ZONE_REMOVE.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup triggerKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        keyboardMarkup.getKeyboard().add(0, createKeyboardRow(ADMIN_SENSOR_ADD.getCommandName(), ADMIN_SENSOR_EDIT.getCommandName(), ADMIN_SENSOR_REMOVE.getCommandName()));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup cameraKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        keyboardMarkup.getKeyboard().add(0, createKeyboardRow(ADMIN_CAMERA_ADD.getCommandName(), ADMIN_CAMERA_EDIT.getCommandName(), ADMIN_SENSOR_REMOVE.getCommandName()));

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

    static ReplyKeyboardMarkup relayKeyboard(List<String> relays) {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        keyboard.add(0, createKeyboardRow(relays.toArray(new String[0])));

        return keyboardMarkup;
    }

    static ReplyKeyboardMarkup relayStateKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = initKeyboard();
        keyboardMarkup.getKeyboard().add(0, createKeyboardRow(RELAY_0, RELAY_50, RELAY_100));
        keyboardMarkup.getKeyboard().add(1, createKeyboardRow(RELAY_10, RELAY_20, RELAY_30, RELAY_40));
        keyboardMarkup.getKeyboard().add(2, createKeyboardRow(RELAY_60, RELAY_70, RELAY_80, RELAY_90));

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

    private static KeyboardRow createKeyboardRow(TelegramCommand... commands) {
        KeyboardRow row = new KeyboardRow();
        Arrays.stream(commands)
                .forEach(command -> row.add(command.getCommandName()));
        return row;
    }
}
