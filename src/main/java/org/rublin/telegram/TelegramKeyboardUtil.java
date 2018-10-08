package org.rublin.telegram;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.rublin.telegram.TelegramCommand.*;

public class TelegramKeyboardUtil {
    public static ReplyKeyboardMarkup mainKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setSelective(true);
        keyboard.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        keyboardRowList.add(createKeyboardRow(SECURITY.getCommandName(), CAMERA.getCommandName()));
        keyboardRowList.add(createKeyboardRow(WEATHER.getCommandName(), MEDIA.getCommandName()));
        keyboardRowList.add(createKeyboardRow(INFO.getCommandName()));

        keyboard.setKeyboard(keyboardRowList);

        return keyboard;
    }

    private static KeyboardRow createKeyboardRow(String ... commands) {
        KeyboardRow row = new KeyboardRow();
        Arrays.stream(commands)
                .forEach(row::add);
        return row;
    }
}
