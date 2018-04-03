package org.rublin.controller;

import org.rublin.model.Zone;
import org.rublin.model.user.User;
import org.rublin.service.TextToSpeechService;
import org.rublin.service.TriggerService;
import org.rublin.service.UserService;
import org.rublin.util.Image;
import org.rublin.util.Resources;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.rublin.util.Resources.USE_MAIL_NOTIFICATION;
import static org.rublin.util.Resources.WEATHER_CITY;
import static org.rublin.util.Resources.WEATHER_LANG;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Send different types of notifications, using other controllers
 *
 * @author Ruslan Sheremet
 * @see EmailController
 * @see ModemController
 * @see TelegramController
 * @since 1.0
 */
@Controller
public class Notification {
    private static final Logger LOG = getLogger(Notification.class);

    @Autowired
    private  EmailController emailController;

    @Autowired
    private  TelegramController telegramController;

    @Autowired
    private  ModemController modemController;

    @Autowired
    private  UserService userService;

    @Autowired
    private  TriggerService triggerService;

    @Autowired
    private SoundController soundController;

    @Autowired
    private TextToSpeechService textToSpeechService;

    @Autowired
    private WeatherController weatherController;

    public void sayTime() {
        LocalTime now = LocalTime.now();
        textToSpeechService.say(String.format("Увага! Поточний час %d годин %d хвилин", now.getHour(), now.getMinute()), "uk");
    }

    public void sayWeather() {
        try {
            Thread.sleep(1000);
            textToSpeechService.say("Доброго ранку." + weatherController.getCondition(WEATHER_CITY, WEATHER_LANG), "uk");
            Thread.sleep(20000);
            textToSpeechService.say(weatherController.getForecast(WEATHER_CITY, WEATHER_LANG), "uk");
        } catch (InterruptedException e) {
            LOG.warn(e.getMessage());
        }

    }

    public void sendEmailNotification(String subject, String message) {
        sendEmail(getEmails(userService.getAll()), subject, message);
    }

    public  void sendInfoToAllUsers(Zone zone) {
        String subject = String.format("Zone %s ", zone.getName());
        String message = String.format("<h1>Zone: <span style=\"color: blue;\">%s</span></h1>\n" +
                        "<h2>Status: <span style=\"color: %s;\">%s</span></h2>\n" +
                        "<h2>Secure: <span style=\"color: %s;\">%s</span></h2>",
                zone.getName(),
                zone.getStatus(),
                zone.getStatus(),
                zone.isSecure() ? "GREEN" : "GREY",
                zone.isSecure());
        List<String> emails = getEmails(userService.getAll());
        sendEmail(emails, subject, message);
        LOG.info("Sending info notification {} to {}", subject, emails);
    }

    public  void sendAlarmNotification(Zone zone, boolean isSecure) {
        Thread sound = new Thread(new Runnable() {
            @Override
            public void run() {
                sendSound(isSecure);
            }
        });
        sound.start();
        String message = triggerService.getInfo(zone);
        String mailBody = String.format("<h2>Zone: <span style=\"color: blue;\">%s</span></h2>\n" +
                        "%s",
                zone.getName(),
                triggerService.getHtmlInfo(zone));
        String subjectHeader;
        List<File> photos = new LinkedList<>();
        if (isSecure) {
             photos.addAll(getPhotos(zone));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LOG.warn(e.getMessage());
            }
            photos.addAll(getPhotos(zone));
            sendTelegram(photos);
            subjectHeader = "Security issue";
        } else {
            subjectHeader = "Other issue";
        }
        String subject = String.format("%s form zone %s", subjectHeader, zone.getShortName());
        sendTelegram(message);
        sendEmail(getEmails(userService.getAll()), subject, mailBody, photos);
        LOG.info("Using sms notification is {}", Resources.USE_SMS);

        if (Resources.USE_SMS) {
            /**
             * Send call notification if it is night (time between 22 and 06)
             */
            LocalDateTime time = LocalDateTime.now();
            if (time.getHour() >= 22 || time.getHour() <= 6) {
                userService.getAll().forEach(
                        user -> sendCall(user.getMobile())
                );
            } else {
                /**
                 * Send sms notification to each user
                 */
                userService.getAll().forEach(
                        user -> sendSms(user.getMobile(), message.replaceAll("<[^>]*>", ""))
                );
            }
        } else {
            /**
             * Send short call notifications
             * 5000 equals to 5 sec
             */
            userService.getAll().forEach(user -> sendCall(user.getMobile(), Resources.CALL_TIMEOUT));
        }
    }

    public void sendCall(String mobile, int i) {
        modemController.call(mobile, i);
    }

    public void sendSound(boolean isSecurity) {
        ClassLoader classLoader = getClass().getClassLoader();
        File security = new File(classLoader.getResource("sound/security_alarm.wav").getFile());
        File other = new File(classLoader.getResource("sound/other_alarm.wav").getFile());
        if (isSecurity) {
            soundController.play(security);
            LOG.info("Played security sound");
        } else {
            soundController.play(other);
            LOG.info("Played other sound");
        }
    }

    private  void sendEmail(List<String> emails, String subject, String message, List<File> files) {
        if (USE_MAIL_NOTIFICATION)
            emailController.sendMailWithAttach(subject, message, files, emails);
    }
    private  void sendEmail(List<String> emails, String subject, String message) {
        if (USE_MAIL_NOTIFICATION)
            emailController.sendMail(subject, message, emails);
    }
    private  void sendSms(String to, String message) {
        modemController.sendSms(to, message);
    }
    private  void sendCall(String to) {
        modemController.call(to);
    }
    private  void sendTelegram(String message) {
        telegramController.sendAlarmMessage(message);
    }
    private  void sendTelegram(List<File> files) {
        telegramController.sendAlarmMessage(files);
    }

    private  List<File> getPhotos(Zone zone) {
        List<File> photos = zone.getCameras().stream()
                .map(Image::getImageFromCamera)
                .collect(Collectors.toList());
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            LOG.warn(e.getMessage());
//        }
//        photos.addAll(zone.getCameras().stream()
//        .map(Image::getImageFromCamera)
//        .collect(Collectors.toList()));
        return photos;
//        List<File> photos = new ArrayList<>();
//        zone.getCameras().forEach(camera -> photos.add(new File(Image.getImageFromCamera(camera))));
//        return photos;
    }

    private  List<String> getEmails(List<User> users) {
        return users.stream()
                .map(user -> user.getEmail())
                .collect(Collectors.toList());
    }
}
