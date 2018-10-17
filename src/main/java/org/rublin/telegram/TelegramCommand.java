package org.rublin.telegram;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public enum TelegramCommand {
    INFO("Info"),

    MAIN("\uD83D\uDD19 Return to main page"),

    ADMIN("\u26D4 Admin"),
    ADMIN_TRIGGERS("Triggers"),
    ADMIN_TRIGGER_ADD("\u2795 Add trigger"),
    ADMIN_TRIGGER_EDIT("\uD83D\uDD27 Edit trigger"),
    ADMIN_TRIGGER_REMOVE("\u2796 Remove trigger"),
    ADMIN_ZONES("\uD83D\uDEC3 Zones"),
    ADMIN_ZONE_ADD("\u2795 Add zone"),
    ADMIN_ZONE_EDIT("\uD83D\uDD27 Edit zone"),
    ADMIN_ZONE_REMOVE("\u2796 Remove zone"),
    ADMIN_CAMERA_ADD("\u2795 Add camera"),
    ADMIN_CAMERA_REMOVE("\u2796 Remove camera"),
    ADMIN_CAMERA_EDIT("\uD83D\uDD27 Edit camera"),

    SECURITY("\u26A1 Security"),
    ARMING("\uD83D\uDD12 Arming"),
    DISARMING("\uD83D\uDD13 Disarming"),
    SECURITY_ALL("\u203C All zones"),

    CAMERA("\uD83D\uDCF7 Camera"),
    CAMERA_ALL("\u203C All cameras"),

    WEATHER("\u2614 Weather"),
    CONDITION("\u2755 Condition"),
    FORECAST("\u2754 Forecast"),

    EVENTS("\uD83D\uDD14 Events"),
    EVENTS_ALL("\u203C Last 10 events"),

    MEDIA("\uD83C\uDFB5 Media"),
    RADIO("\uD83D\uDCFB Radio"),
    STOP("\u25FE Stop"),
    SAY("\uD83C\uDFA4 Say"),
    LANGUAGE_UK("UK"),
    LANGUAGE_EN("EN"),
    LANGUAGE_DE("DE"),
    LANGUAGE_OTHER("Other"),
    VOLUME("\uD83D\uDD0A Volume"),
    VOLUME_UP("\u2B06 Volume UP"),
    VOLUME_DOWN("\u2B07 Volume DOWN");

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
            Optional<TelegramCommand> byName = Arrays.stream(TelegramCommand.values())
                    .filter(telegramCommand -> telegramCommand.getCommandName().equalsIgnoreCase(commandName))
                    .findFirst();
            if (!byName.isPresent())
                log.warn("Command {} not found: {}", commandName, throwable.getMessage());
            command = byName.orElse(null);
        }
        if (Objects.nonNull(command))
            log.info("Found {} command", command);
        return command;
    }
}
