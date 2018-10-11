package org.rublin.telegram;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public enum TelegramCommand {
    INFO("Info"),

    MAIN("Return to main page"),

    SECURITY("Security"),
    ARMING("Arming"),
    DISARMING("Disarming"),
    SECURITY_ALL("All zones"),

    CAMERA("Camera"),
    CAMERA_ALL("All cameras"),

    WEATHER("Weather"),
    CONDITION("Condition"),
    FORECAST("Forecast"),

    MEDIA("Media"),
    RADIO("Radio"),
    STOP("Stop"),
    SAY("Say"),
    LANGUAGE_UK("UK"),
    LANGUAGE_EN("EN"),
    LANGUAGE_DE("DE"),
    LANGUAGE_OTHER("Other"),
    VOLUME("Volume"),
    VOLUME_UP("Volume UP"),
    VOLUME_DOWN("Volume DOWN");

    private final String commandName;

    TelegramCommand(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public static TelegramCommand fromCommandName(String commandName) {
        TelegramCommand command = null;
        try {
            command = TelegramCommand.valueOf(commandName.toUpperCase());

        } catch (Throwable throwable) {
            log.warn("Command {} not found: ", commandName, throwable);
            Optional<TelegramCommand> byName = Arrays.stream(TelegramCommand.values())
                    .filter(telegramCommand -> telegramCommand.getCommandName().equalsIgnoreCase(commandName))
                    .findFirst();
            command = byName.orElse(null);
        }

        return command;
    }
}
