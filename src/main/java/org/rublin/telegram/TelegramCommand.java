package org.rublin.telegram;

public enum TelegramCommand {
    INFO("Info"),

    SECURITY("Security"),
    ARMING("Arming"),
    DISARMING("Disarming"),

    CAMERA("Camera"),
    WEATHER("Weather"),
    MEDIA("Media");

    private final String commandName;

    TelegramCommand(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
