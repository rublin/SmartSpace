package org.rublin.service;

import org.rublin.controller.TelegramController;
import org.rublin.model.Camera;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.repository.ZoneRepository;
import org.rublin.util.Notification;
import org.rublin.util.exception.ExceptionUtil;
import org.rublin.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 11.07.2016.
 */
@Service
public class ZoneServiceImpl implements ZoneService {

    private static final Logger LOG = getLogger(ZoneServiceImpl.class);

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private TriggerService triggerService;

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramController telegramController;

    @Override
    public Zone save(Zone zone) {
        return zoneRepository.save(zone);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(zoneRepository.delete(id), id);
    }

    @Override
    public Zone get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(zoneRepository.get(id), id);
    }

    @Override
    public Collection<Zone> getAll() {
        return zoneRepository.getAll();
    }

    @Override
    public void setSecure(Zone zone, boolean security) {
        zone.setSecure(security);
        if (!security) {
            zone.setStatus(ZoneStatus.GREEN);
        }
        zoneRepository.save(zone);
        LOG.info("change Zone secure state to {}", zone.isSecure());
        Notification.sendMail(String.format("Zone %s ", zone.getName()),
                String.format("<h1>Zone: <span style=\"color: blue;\">%s</span></h1>\n" +
                                "<h2>Status: <span style=\"color: %s;\">%s</span></h2>\n" +
                                "<h2>Secure: <span style=\"color: %s;\">%s</span></h2>",
                        zone.getName(),
                        zone.getStatus(),
                        zone.getStatus(),
                        zone.isSecure() ? "GREEN" : "GREY",
                        zone.isSecure()), userService.getAll());
    }

    @Override
    public Zone setStatus(Zone zone, ZoneStatus status) {
        zone.setStatus(status);
        zoneRepository.save(zone);
 /*       List<File> photos = getPhotos(zone);
        String message = String.format("<h2>Zone: <span style=\"color: blue;\">%s</span></h2>\n" +
                        "%s",
                zone.getName(),
                triggerService.getHtmlInfo(zone));
        telegramController.sendAlarmMessage(triggerService.getInfo(zone));
        telegramController.sendAlarmMessage(photos);
        Notification.sendMailWithAttach(String.format("Zone %s activity", zone.getName()),
                message, photos, userService.getAll());*/
        return zone;
    }

    @Override
    public String getInfo(Zone zone) {
        return String.format(
                "id: <b>%d</b>, name: <b>%s</b>, status: <b>%s</b>, secure: <b>%s</b>",
                zone.getId(),
                zone.getName(),
                zone.getStatus().toString(),
                zone.isSecure() ? "YES" : "NO");    }

    @Override
    public void sendNotification(Zone zone) {
        String subject = String.format("Zone %s (%s) triggers notification", zone.getName(), zone.getShortName());
        String mailBody = String.format("<h2>Zone: <span style=\"color: blue;\">%s</span></h2>\n" +
                        "%s",
                zone.getName(),
                triggerService.getHtmlInfo(zone));
        List<File> photos = getPhotos(zone);
        telegramController.sendAlarmMessage(triggerService.getInfo(zone));
        telegramController.sendAlarmMessage(photos);
        Notification.sendMailWithAttach(subject,
                mailBody, photos, userService.getAll());
        LOG.info("Notification sending");
    }

    private List<File> getPhotos(Zone zone) {
        List<File> photos = new ArrayList<>();
        zone.getCameras().forEach(camera -> photos.add(new File(Notification.getImageFromCamera(camera))));
        return photos;
    }
}
