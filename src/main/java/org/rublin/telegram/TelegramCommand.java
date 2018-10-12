package org.rublin.telegram;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public enum TelegramCommand {
    INFO("Info"),

    MAIN("Return to main page"),

    ADMIN("Admin"),
    ADMIN_TRIGGERS("Triggers"),
    ADMIN_TRIGGER_ADD("Add trigger"),
    ADMIN_TRIGGER_EDIT("Edit trigger"),
    ADMIN_TRIGGER_REMOVE("Remove trigger"),
    ADMIN_ZONES("Zones"),
    ADMIN_ZONE_ADD("Add zone"),
    ADMIN_ZONE_EDIT("Edit zone"),
    ADMIN_ZONE_REMOVE("Remove zone"),

    SECURITY("Security"),
    ARMING("Arming"),
    DISARMING("Disarming"),
    SECURITY_ALL("All zones"),

    CAMERA("Camera"),
    CAMERA_ALL("All cameras"),

    WEATHER("Weather"),
    CONDITION("Condition"),
    FORECAST("Forecast"),

    EVENTS("Events"),
    EVENTS_ALL("Last 10 events"),

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
