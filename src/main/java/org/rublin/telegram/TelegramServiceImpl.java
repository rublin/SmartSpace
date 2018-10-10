package org.rublin.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.model.Camera;
import org.rublin.model.ConfigKey;
import org.rublin.model.Trigger;
import org.rublin.model.Zone;
import org.rublin.model.event.Event;
import org.rublin.service.*;
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
import static org.rublin.telegram.TelegramKeyboardUtil.*;

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
    private final EventService eventService;
    private final SystemConfigService configService;
    private Map<Long, TelegramCommand> previousCommandMap = new ConcurrentHashMap<>();


    @Value("${weather.city}")
    private String city;

    @Value("${weather.lang}")
    private String lang;

    @Value("${tmp.directory}")
    private String tmpDir;

    @Override
    public TelegramResponseDto process(Message message) {
        log.info(message.getText().substring(0, 3).toLowerCase());
        if (message.getText().startsWith("/")) {
            return doClassicCommand(message);
        } else {
            return doKeyboardCommand(message);
        }
    }

    private TelegramResponseDto doKeyboardCommand(Message message) {
        String id = message.getChatId().toString();
        List<String> responseMessages = new ArrayList<>();
        List<File> responseFiles = new ArrayList<>();
        ReplyKeyboardMarkup keyboardMarkup = mainKeyboard();
        TelegramCommand command = TelegramCommand.fromCommandName(message.getText());
        TelegramCommand previousCommand = previousCommandMap.get(message.getChatId());
        if (Objects.nonNull(previousCommand)) {
            switch (previousCommand) {
                case ARMING:
                    if (message.getText().equals("All")) {
                        zoneService.getAll().forEach(
                                zone -> {
                                    zoneService.setSecure(zone, true);
                                    responseMessages.add(format(
                                            "Zone <b>%s</b> is <b>arming</b> now", zone.getName()));
                                    responseMessages.add(zoneService.getInfo(zone));
                                }
                        );
                    } else {
                        log.info("Need to find zone by name {}", message.getText());
                    }
            }
        }

        if (Objects.nonNull(command)) {
            switch (command) {
                case INFO:
                    responseMessages.add("Some information");
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
//                    previousCommandMap.put(message.getChatId(), command);
                    break;
                case DISARMING:
                    responseMessages.add("Which zone do you want disarming");
                    keyboardMarkup = armingOrDisarmingKeyboard(
                            zoneService.getAll().stream()
                                    .map(Zone::getName)
                                    .collect(toList()));
//                    previousCommandMap.put(message.getChatId(), command);
                    break;
                case MEDIA:
                    keyboardMarkup = mediaKeyboard();
                    previousCommandMap.put(message.getChatId(), command);
                    break;
                case WEATHER:
                    keyboardMarkup = weatherKeyboard();
                    previousCommandMap.put(message.getChatId(), command);
                    break;
                case CAMERA:
                    keyboardMarkup = cameraKeyboard(
                            cameraService.getAll().stream()
                                    .map(Camera::getName)
                                    .collect(toList()));
                    previousCommandMap.put(message.getChatId(), command);
                    break;
            }
        }
        if (responseMessages.isEmpty() && responseFiles.isEmpty()) {
            responseMessages.add("Select the next step");
        }
        return TelegramResponseDto.builder()
                .id(message.getChatId().toString())
                .messages(responseMessages)
                .files(responseFiles)
                .keyboard(keyboardMarkup)
                .build();
    }

    private TelegramResponseDto doClassicCommand(Message message) {
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
                String forecast = weatherService.getForecast(city, lang);
                textToSpeechService.say(forecast, "uk");
                responseMessages.add(forecast);
                break;
            }
            case "/wc": {
                String condition = weatherService.getCondition(city, lang);
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
                .keyboard(mainKeyboard())
                .build();
    }
}
