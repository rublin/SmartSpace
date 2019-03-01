package org.rublin.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.message.NotificationMessage;
import org.rublin.model.Camera;
import org.rublin.model.ConfigKey;
import org.rublin.model.Trigger;
import org.rublin.model.Zone;
import org.rublin.model.event.Event;
import org.rublin.model.sensor.TemperatureSensor;
import org.rublin.model.user.User;
import org.rublin.service.*;
import org.rublin.service.delayed.DelayQueueService;
import org.rublin.service.impl.TemperatureServiceImpl;
import org.rublin.to.TelegramResponseDto;
import org.rublin.util.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.rublin.telegram.TelegramCommand.*;
import static org.rublin.telegram.TelegramKeyboardUtil.*;
import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private final ZoneService zoneService;
    private final CameraService cameraService;
    private final WeatherService weatherService;
    private final TextToSpeechService textToSpeechService;
    private final MediaPlayerService mediaPlayerService;
    private final TriggerService triggerService;
    private final TemperatureServiceImpl temperatureService;
    private final EventService eventService;
    private final SystemConfigService configService;
    private final UserService userService;
    private final HeatingService heatingService;
    private final DelayQueueService delayQueueService;


    private Map<Long, TelegramCommand> previousCommandMap = new ConcurrentHashMap<>();

    @Value("${tmp.directory}")
    private String tmpDir;

    @Override
    public TelegramResponseDto process(Message message) {
        log.info(message.getText());
        User user = authentication(message);
        if (Objects.isNull(user)) {
            return TelegramResponseDto.builder()
                    .messages(Collections.singletonList("The user is not authenticated"))
                    .build();
        }
        if (message.getText().startsWith("/")) {
            return doClassicCommand(message, user);
        } else {
            return doKeyboardCommand(message, user);
        }
    }

    @Override
    public Set<Integer> getChatIds() {
        Set<Integer> collect = userService.getAll().stream()
                .map(User::getTelegramId)
                .filter(Objects::nonNull)
                .collect(toSet());
        return collect;
    }

    private User authentication(Message message) {
        int telegramId = message.getFrom().getId();
        log.debug("Received message from user {} (id: {})", message.getFrom().getUserName(), telegramId);
        User user = null;
        try {
            user = userService.getByTelegramId(telegramId);
            log.info("Found user by id {}", telegramId);
        } catch (Throwable throwable) {
            log.warn("Could not find user by id {}", telegramId);
            user = userService.getByTelegramName(message.getFrom().getUserName());
            log.info("Found user by name {}", user.getTelegramName());
            checkTelegramId(user, telegramId);
        }
        return user;
    }

    private void checkTelegramId(User user, int id) {
        user.setTelegramId(id);
        userService.save(user);
    }

    private TelegramResponseDto doKeyboardCommand(Message message, User user) {
        Long id = message.getChatId();
        List<String> responseMessages = new ArrayList<>();
        List<File> responseFiles = new ArrayList<>();
        ReplyKeyboardMarkup keyboardMarkup = mainKeyboard(user);
        TelegramCommand command = TelegramCommand.fromCommandName(message.getText());
        TelegramCommand previousCommand = previousCommandMap.get(id);

        if (Objects.nonNull(command)) {
            if (!authorize(user, command)) {
                return TelegramResponseDto.builder()
                        .messages(Collections.singletonList("The user is not authorized to use this command"))
                        .build();
            }
            switch (command) {
                case INFO:
                    responseMessages.add("Some information");
                    break;

                case MAIN:
                    previousCommandMap.remove(id);
                    break;

                case ADMIN:
                    keyboardMarkup = adminKeyboard();
                    break;

                case ADMIN_ZONES:
                    keyboardMarkup = zonesKeyboard();
                    zoneService.getAll().forEach(
                            zone -> responseMessages.add(format("%d; %s; %s", zone.getId(), zone.getName(), zone.getStatus()))
                    );
                    break;

                case ADMIN_TEMPERATURE_SENSOR_ADD:
                    keyboardMarkup = null;
                    responseMessages.add("Type sensor parameters in format:\n ZoneId#Name#Min#Max");
                    previousCommandMap.put(id, command);
                    break;

                case ADMIN_SENSORS:
                    keyboardMarkup = triggerKeyboard();
                    triggerService.getAll().forEach(
                            trigger -> responseMessages.add(format("%d; %s; %s; %s", trigger.getId(), trigger.getName(), trigger.getType().name(), trigger.getZone().getName()))
                    );
                    break;

                case ADMIN_CAMERAS:
                    keyboardMarkup = cameraKeyboard();
                    cameraService.getAll().forEach(
                            camera -> responseMessages.add(format("%d; %s; %s; %s", camera.getId(), camera.getName(), camera.getIp(), camera.getZone().getName()))
                    );
                    break;

                case SECURITY:
                    zoneService.getAll().forEach(
                            zone -> responseMessages.add(zoneService.getInfo(zone))
                    );
                    keyboardMarkup = securityKeyboard();
                    break;

                case ARMING:
                    responseMessages.add("Which zone do you want arming");
                    keyboardMarkup = armingOrDisarmingKeyboard(
                            zoneService.getAll().stream()
                                    .map(Zone::getName)
                                    .collect(toList()));
                    previousCommandMap.put(id, command);
                    break;

                case DISARMING:
                    responseMessages.add("Which zone do you want disarming");
                    keyboardMarkup = armingOrDisarmingKeyboard(
                            zoneService.getAll().stream()
                                    .map(Zone::getName)
                                    .collect(toList()));
                    previousCommandMap.put(id, command);
                    break;

                case SECURITY_ALL:
                    boolean security = ARMING == previousCommand;
                    zoneService.getAll().forEach(
                            zone -> {
                                zoneService.setSecure(zone, security);
                                responseMessages.add(format(
                                        "Zone <b>%s</b> is <b>%s</b> now", zone.getName(),
                                        security ? "arming" : "disarming"));
                                responseMessages.add(zoneService.getInfo(zone));
                            }
                    );
                    previousCommandMap.remove(id);
                    break;

                case HEATING:
                    responseMessages.add(heatingService.current().isGlobalStatus() ? "Pump is ON" : "Pump is OFF");
                    keyboardMarkup = heatingKeyboard();
                    break;

                case HEATING_PUMP_ON:
                    responseMessages.add(heatingService.pump(true));
                    break;

                case HEATING_PUMP_OFF:
                    heatingService.stopHeating();
                    responseMessages.add(heatingService.current().status());
                    break;

                case EVENTS:
                    keyboardMarkup = eventsKeyboard(
                            triggerService.getAll().stream()
                                    .map(Trigger::getName)
                                    .collect(toList()));
                    List<String> eventMessages = triggerService.getAll().stream()
                            .map(t -> eventService.get(t, 5))
                            .flatMap(List::stream)
                            .sorted(Comparator.comparing(Event::getTime))
                            .map(e -> format("%s - %s: %s",
                                    e.getTime().format(DateTimeFormatter.ISO_DATE_TIME),
                                    e.getTrigger().getName(),
                                    e.getState()))
                            .collect(toList());
                    responseMessages.addAll(eventMessages);

                    previousCommandMap.put(id, command);
                    break;

                case MEDIA:
                    keyboardMarkup = mediaKeyboard();
                    previousCommandMap.put(id, command);
                    break;

                case WEATHER:
                    keyboardMarkup = weatherKeyboard();
                    break;

                case FORECAST:
                    List<String> forecast = weatherService.getForecast();
                    for (int i = 0; i < forecast.size(); i++) {
                        delayQueueService.put(new NotificationMessage(textToSpeechService.prepareFile(forecast.get(i), "uk"), i * 15));
                        responseMessages.add(forecast.get(i));
                    }
                    break;

                case CONDITION:
                    String condition = weatherService.getCondition();
                    textToSpeechService.say(condition, "uk");
                    responseMessages.add(condition);
                    break;

                case RADIO:
                    mediaPlayerService.play(configService.get(ConfigKey.RADIO), Integer.parseInt(configService.get(ConfigKey.MUSIC_VOLUME)));
                    break;

                case STOP:
                    mediaPlayerService.stop();
                    break;

                case VOLUME:
                    responseMessages.add("Changing volume is only possible when the music is playing");
                    keyboardMarkup = volumeKeyboard();
                    break;

                case VOLUME_UP:
                    mediaPlayerService.setVolume(true);
                    keyboardMarkup = volumeKeyboard();
                    break;

                case VOLUME_DOWN:
                    mediaPlayerService.setVolume(false);
                    keyboardMarkup = volumeKeyboard();
                    break;

                case SAY:
                    responseMessages.add("Select your language or just use your default language");
                    previousCommandMap.put(id, command);
                    keyboardMarkup = sayKeyboard();
                    break;

                case LANGUAGE_UK:
                    responseMessages.add("Type what you want me to say");
                    previousCommandMap.put(id, command);
                    break;

                case LANGUAGE_EN:
                    responseMessages.add("Type what you want me to say");
                    previousCommandMap.put(id, command);
                    break;

                case LANGUAGE_DE:
                    responseMessages.add("Type what you want me to say");
                    previousCommandMap.put(id, command);
                    break;

                case LANGUAGE_OTHER:
                    responseMessages.add("Type language code and what you want me to say as new line. \nLike this:\n\nEN\nGlory to Ukraine!");
                    previousCommandMap.put(id, command);
                    break;

                case CAMERA:
                    keyboardMarkup = cameraKeyboard(
                            cameraService.getAll().stream()
                                    .map(Camera::getName)
                                    .collect(toList()));
                    previousCommandMap.put(id, command);
                    break;

                case CAMERA_ALL:
                    cameraService.getAll().forEach(
                            camera -> responseFiles.add(Image.getImageFromCamera(camera, tmpDir)));
                    previousCommandMap.remove(id);
                    break;
            }
        }

        if (Objects.nonNull(previousCommand) && Objects.isNull(command)) {
            if (previousCommand == ARMING || previousCommand == DISARMING) {
                boolean security = previousCommand == ARMING;
                Zone zone = zoneService.getAll().stream()
                        .filter(z -> z.getName().equalsIgnoreCase(message.getText()))
                        .findFirst().orElse(null);
                if (Objects.nonNull(zone)) {
                    zoneService.setSecure(zone, security);
                    responseMessages.add(format(
                            "Zone <b>%s</b> is <b>%s</b> now",
                            zone.getName(),
                            security ? "arming" : "disarming"));
                    responseMessages.add(zoneService.getInfo(zone));
                } else {
                    log.warn("Need to find zone by name {}", message.getText());
                    responseMessages.add(format("Can't find zone with name %s", message.getText()));
                }
                previousCommandMap.remove(id);
            } else if (previousCommand == CAMERA) {
                Camera camera = cameraService.getAll().stream()
                        .filter(c -> c.getName().equalsIgnoreCase(message.getText()))
                        .findFirst().orElse(null);
                if (Objects.nonNull(camera)) {
                    responseFiles.add(Image.getImageFromCamera(camera, tmpDir));
                }
            } else if (previousCommand == SAY) {
                textToSpeechService.say(message.getText(), "uk");
                previousCommandMap.remove(id);
            } else if (previousCommand == LANGUAGE_DE || previousCommand == LANGUAGE_EN || previousCommand == LANGUAGE_UK) {
                String languageCode = previousCommand.name().split("_")[1].toLowerCase();
                textToSpeechService.say(message.getText(), languageCode);
                previousCommandMap.remove(id);
            } else if (previousCommand == LANGUAGE_OTHER) {
                String[] split = message.getText().split("\n");
                if (split.length == 2)
                    textToSpeechService.say(split[1].trim(), split[0].trim().toLowerCase());
                else
                    responseMessages.add("I expect only 2 lines");
                previousCommandMap.remove(id);
            } else if (previousCommand == EVENTS) {
                previousCommandMap.remove(id);
                Optional<Trigger> optionalTrigger = triggerService.getAll().stream()
                        .filter(t -> t.getName().equalsIgnoreCase(message.getText()))
                        .findFirst();
                if (optionalTrigger.isPresent()) {
                    List<Event> events = eventService.get(optionalTrigger.get(), 5);
                    events.forEach(e -> responseMessages.add(format("%s - %s: %s",
                            e.getTime().format(DateTimeFormatter.ISO_DATE_TIME),
                            e.getTrigger().getName(),
                            e.getState())));
                } else {
                    responseMessages.add(format("Trigger %s not found", message.getText()));
                }
            } else if (previousCommand == ADMIN_TEMPERATURE_SENSOR_ADD) {
                previousCommandMap.remove(id);
                String text = message.getText();
                if (isEmpty(text)) {
                    responseMessages.add("Message cannot me empty");
                } else {
                    String[] split = text.split("#");
                    if (split.length == 4) {
                        try {
                            Integer zoneId = Integer.valueOf(split[0]);
                            String name = split[1];
                            double min = Double.parseDouble(split[2]);
                            double max = Double.parseDouble(split[3]);
                            Zone zone = new Zone();
                            zone.setId(zoneId);
                            TemperatureSensor sensor = new TemperatureSensor();
                            sensor.setZone(zone);
                            sensor.setName(name);
                            sensor.setMinThreshold(min);
                            sensor.setMaxThreshold(max);
                            TemperatureSensor save = temperatureService.save(sensor);
                            responseMessages.add(format("Saved sensor with name %s and id %d",
                                    save.getName(),
                                    save.getId()));
                        } catch (Throwable throwable) {
                            log.error("Failed to create new sensor", throwable);
                            responseMessages.add(format("Failed to create new sensor: %s", throwable.getMessage()));
                        }

                    } else {
                        responseMessages.add(format("%d is not valid amount of parameters. Please use valid format", split.length));
                    }
                }

            }
        }

        if (responseMessages.isEmpty() && responseFiles.isEmpty()) {
            responseMessages.add("Select the next step");
        }
        return TelegramResponseDto.builder()
                .id(id.toString())
                .messages(responseMessages)
                .files(responseFiles)
                .keyboard(keyboardMarkup)
                .build();
    }

    private boolean authorize(User user, TelegramCommand command) {
        if (command.name().startsWith("ADMIN")) {
            return user.isAdmin();
        }
        return true;
    }

    @Deprecated
    private TelegramResponseDto doClassicCommand(Message message, User user) {
        List<String> responseMessages = new ArrayList<>();
        List<File> responseFiles = new ArrayList<>();
        switch (message.getText().substring(0, 3).toLowerCase()) {
            case "/aa": {
                Collection<Zone> zones = zoneService.getAll();
                for (Zone zone : zones) {
                    zoneService.setSecure(zone, true);
                    responseMessages.add(format(
                            "Zone <b>%s</b> is <b>arming</b> now", zone.getName()));
                    responseMessages.add(zoneService.getInfo(zone));
                }
                break;
            }
            case "/da": {
                Collection<Zone> zones = zoneService.getAll();
                for (Zone zone : zones) {
                    zoneService.setSecure(zone, false);
                    responseMessages.add(format("Zone <b>%s</b> is <b>disarming</b> now", zone.getName()));
                    responseMessages.add(zoneService.getInfo(zone));
                }
                break;
            }
            case "/ss":
                if (message.getText().matches("\\/ss\\s\\d+\\s[0-1]")) {
                    String[] command = message.getText().split(" ");
                    try {
                        Zone zone = zoneService.get(Integer.parseInt(command[1]));
                        zoneService.setSecure(zone, command[2].equals("0") ? Boolean.FALSE : Boolean.TRUE);
                        responseMessages.add(format("Zone <b>%s</b> changed security to <b>%s</b>", zone.getName(),
                                zone.isSecure()));
                        responseMessages.add(zoneService.getInfo(zone));
                    } catch (Exception e) {
                        log.error("Error to change state for zone {} to {}", command[1], command[2]);
                        responseMessages.add("Can't find zone with id: " + command[1]);
                    }
                } else {
                    responseMessages.add(format(
                            "Command %s is not support. " +
                                    "Use format like <b>/ss id 1</b> to arming, " +
                                    "and <b>/ss id 0</b> to disarming. Do not forget to change id to current zone id!",
                            message.getText()));
                }
                break;
            case "/ca": {
                cameraService.getAll().forEach(
                        camera -> responseFiles.add(Image.getImageFromCamera(camera, tmpDir)));
            }
            break;
            case "/gs": {
                Collection<Zone> zones = zoneService.getAll();
                for (Zone zone : zones) {
                    responseMessages.add(zoneService.getInfo(zone));
                }
                break;

            }
            case "/wf": {
                weatherService.getForecast().forEach(f -> {
                    textToSpeechService.say(f, "uk");
                    responseMessages.add(f);
                });
                break;
            }
            case "/wc": {
                String condition = weatherService.getCondition();
                textToSpeechService.say(condition, "uk");
                responseMessages.add(condition);
                break;
            }
            case "/pl": {
                mediaPlayerService.play(configService.get(ConfigKey.RADIO), Integer.parseInt(configService.get(ConfigKey.MUSIC_VOLUME)));
                break;
            }

            case "/st": {
                mediaPlayerService.stop();
                break;
            }

            case "/sv": {
                String[] split = message.getText().split(" ");
                mediaPlayerService.setVolume(Integer.valueOf(split[1]), 0);
                break;
            }

            case "/sa": {
                String[] split = message.getText().split("\n");
                if (split.length > 1)
                    textToSpeechService.say(split[2].trim(), split[1].trim());
                else if (split.length == 1)
                    textToSpeechService.say(split[1], "uk");
                break;
            }

            case "/ts": {
                if (message.getText().matches("\\/ts\\s\\d+")) {
                    String[] split = message.getText().split(" ");
                    try {
                        Trigger trigger = triggerService.get(Integer.valueOf(split[1]));
                        List<Event> events = eventService.get(trigger, 5);
                        events.forEach(e -> responseMessages.add(format("%s - %s: %s",
                                e.getTime().format(DateTimeFormatter.ISO_DATE_TIME),
                                e.getTrigger().getName(),
                                e.getState())));
                    } catch (Throwable throwable) {
                        log.info("", throwable);
                        responseMessages.add(throwable.getMessage());
                    }
                } else {
                    triggerService.getAll().forEach(t -> responseMessages.add(t.toString()));
                }
                break;
            }

            case "/tr": {
                if (message.getText().matches("\\/tr\\s\\d+\\s[0-1]")) {
                    String[] split = message.getText().split(" ");
                    try {
                        Trigger trigger = triggerService.get(Integer.valueOf(split[1]));
                        trigger.setActive(Boolean.valueOf(split[2]));
                        triggerService.save(trigger, trigger.getZone());
                        String logMessage = format("Set active to %s for trigger %s", trigger.isActive(), trigger.getName());
                        log.info(logMessage);
                        responseMessages.add(logMessage);
                    } catch (Throwable throwable) {
                        log.info("", throwable);
                        responseMessages.add(throwable.getMessage());
                    }
                } else {
                    responseMessages.add(format(
                            "Command %s is not support. " +
                                    "Use format like <b>/tr id 1</b> to activate, " +
                                    "and <b>/ss id 0</b> to disactivate. Do not forget to change id to current trigger id!",
                            message.getText()));
                }
                break;
            }

            default:
                responseMessages.add("Your command does not support. Try to use /help");
        }
        return TelegramResponseDto.builder()
                .id(message.getChatId().toString())
                .messages(responseMessages)
                .files(responseFiles)
                .keyboard(mainKeyboard(user))
                .build();
    }
}
