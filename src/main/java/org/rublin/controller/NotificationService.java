package org.rublin.controller;

import lombok.extern.slf4j.Slf4j;
import org.rublin.model.Zone;
import org.rublin.model.user.User;
import org.rublin.service.*;
import org.rublin.telegram.TelegramController;
import org.rublin.util.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationHome;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Send different types of notifications, using other controllers
 *
 * @author Ruslan Sheremet
 * @see EmailController
 * @see ModemController
 * @see TelegramController
 * @since 1.0
 */

@Slf4j
@Service
public class NotificationService {

    @Value("${sound.security}")
    public String securityAlarm = "sound/security_alarm.wav";
    @Value("${sound.other}")
    public String otherAlarm = "sound/other_alarm.wav";
    @Value("${tmp.directory}")
    private String tmpDir;

    @Autowired
    private  EmailController emailController;
    @Autowired
    private  TelegramController telegramController;
    @Autowired
    private  ModemController modemController;
    @Autowired
    private UserService userService;
    @Autowired
    private TriggerService triggerService;
    @Autowired
    private MediaPlayerService player;
    @Autowired
    private TextToSpeechService textToSpeechService;
    @Autowired
    private WeatherService weatherService;

    @Value("${mail.notification}")
    private boolean mailNotification;

    @Value("${modem.sms}")
    private boolean smsNotification;

    @Value("${modem.call_timeout}")
    private int callTimeout;

    @Value("${weather.city}")
    private String city;

    @Value("${weather.lang}")
    private String lang;
    
    public void sayTime() {
        LocalTime now = LocalTime.now();
        textToSpeechService.say(String.format("Увага! Поточний час %d годин %d хвилин", now.getHour(), now.getMinute()), "uk");
    }

    public void sayWeather() {
        try {
            Thread.sleep(1000);
            textToSpeechService.say("Доброго ранку." + weatherService.getCondition(city, lang), "uk");
            Thread.sleep(20000);
            textToSpeechService.say(weatherService.getForecast(city, lang), "uk");
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
        }

    }

    public void sendEmailNotification(String subject, String message) {
        sendEmail(getEmails(userService.getAll()), subject, message);
    }

    public void sendInfoToAllUsers(Zone zone) {
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
        log.info("Sending info notification {} to {}", subject, emails);
    }

    public void sendAlarmNotification(Zone zone, boolean isSecure) {
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
                log.warn(e.getMessage());
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
        log.info("Using sms notification is {}", smsNotification);

        if (smsNotification) {
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
            userService.getAll().forEach(user -> sendCall(user.getMobile(), callTimeout));
        }
    }

    public void sendCall(String mobile, int i) {
        modemController.call(mobile, i);
    }

    public void sendSound(boolean isSecurity) {
        ApplicationHome home = new ApplicationHome(this.getClass());
        File security = null;
        File other = null;
        security = new File(home.getDir(), securityAlarm);
//                new File(Objects.requireNonNull(classLoader.getResource(securityAlarm)).getFile());
        other = new File(home.getDir(), otherAlarm);
//                new File(Objects.requireNonNull(classLoader.getResource(otherAlarm)).getFile());
        if (isSecurity) {
            player.play(security.getPath(), 80);
            log.info("Played security sound");
        } else {
            player.play(other.getPath(), 80);
            log.info("Played other sound");
        }
    }

    private void sendEmail(List<String> emails, String subject, String message, List<File> files) {
        if (mailNotification)
            emailController.sendMailWithAttach(subject, message, files, emails);
    }

    private void sendEmail(List<String> emails, String subject, String message) {
        if (mailNotification)
            emailController.sendMail(subject, message, emails);
    }

    private void sendSms(String to, String message) {
        modemController.sendSms(to, message);
    }

    private void sendCall(String to) {
        modemController.call(to);
    }

    private void sendTelegram(String message) {
        telegramController.sendAlarmMessage(message);
    }

    private void sendTelegram(List<File> files) {
        telegramController.sendAlarmMessage(files);
    }

    private List<File> getPhotos(Zone zone) {
        List<File> photos = zone.getCameras().stream()
                .map(camera -> Image.getImageFromCamera(camera, tmpDir))
                .collect(Collectors.toList());
        return photos;
    }

    private List<String> getEmails(List<User> users) {
        return users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    @PostConstruct
    private void init() throws InterruptedException {
        sendSound(true);
        Thread.sleep(1000L);
        sendSound(false);
        Thread.sleep(1000L);
        player.stop();
    }
}
