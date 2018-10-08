package org.rublin.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.String.format;

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


    @Value("${weather.city}")
    private String city;

    @Value("${weather.lang}")
    private String lang;

    @Value("${tmp.directory}")
    private String tmpDir;

    @Override
    public TelegramResponseDto process(Message message) {
        log.info(message.getText().substring(0, 3).toLowerCase());
        String id = message.getChatId().toString();
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
        return createResponse(id, responseMessages, responseFiles);
    }

    private TelegramResponseDto createResponse(String id, List<String> messages, List<File> files) {
        return TelegramResponseDto.builder()
                .messages(messages)
                .files(files)
                .id(id)
                .build();
    }
}
