package org.rublin.controller;

import org.rublin.controller.EmailController;
import org.rublin.controller.ModemController;
import org.rublin.controller.TelegramController;
import org.rublin.model.Zone;
import org.rublin.model.user.User;
import org.rublin.service.TriggerService;
import org.rublin.service.UserService;
import org.rublin.util.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.rublin.util.Resources.USE_MAIL_NOTIFICATION;

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
    }

    public  void sendAlarmNotification(Zone zone) {
        List<File> photos = getPhotos(zone);
        String message = triggerService.getInfo(zone);
        String subject = String.format("Zone %s (%s) triggers notification", zone.getName(), zone.getShortName());
        String mailBody = String.format("<h2>Zone: <span style=\"color: blue;\">%s</span></h2>\n" +
                        "%s",
                zone.getName(),
                triggerService.getHtmlInfo(zone));
        sendTelegram(message);
        sendTelegram(photos);
        sendEmail(getEmails(userService.getAll()), subject, mailBody, photos);
        userService.getAll().forEach(
                user -> sendSms(user.getMobile(), message.replaceAll("<[^>]*>", ""))
        );

        /**
         * Send call notification if is night (time between 22 and 06)
         */
        LocalDateTime time = LocalDateTime.now();
        if (time.getHour() >= 22 || time.getHour() <= 6) {
            userService.getAll().forEach(
                    user -> sendCall(user.getMobile())
            );
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
        return zone.getCameras().stream()
                .map(camera -> Image.getImageFromCamera(camera))
                .collect(Collectors.toList());
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
