package org.rublin.service;

import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.repository.ZoneRepository;
import org.rublin.util.Notification;
import org.rublin.util.exception.ExceptionUtil;
import org.rublin.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
        LOG.info("change Zone secure state to {}", zone.getSecure());
        Notification.sendMail(String.format("Zone %s ", zone.getName()),
                String.format("<h1>Zone: <span style=\"color: blue;\">%s</span></h1>\n" +
                                "<h2>Status: <span style=\"color: %s;\">%s</span></h2>\n" +
                                "<h2>Secure: <span style=\"color: %s;\">%s</span></h2>",
                        zone.getName(),
                        zone.getStatus(),
                        zone.getStatus(),
                        zone.getSecure() ? "GREEN" : "GREY",
                        zone.getSecure()));
    }

    @Override
    public Zone setStatus(Zone zone, ZoneStatus status) {
        zone.setStatus(status);
        zoneRepository.save(zone);
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        Notification.sendMailWithAttach(String.format("Zone %s activity", zone.getName()),
                String.format("<h2>Zone: <span style=\"color: blue;\">%s</span></h2>\n" +
                                "%s",
                        zone.getName(),
                        triggerService.getInfo(zone)), "http://192.168.0.31/Streaming/channels/1/picture");
        return zone;
    }

    @Override
    public String getInfo(Zone zone) {
        return String.format(
                "id: <b>%d</b>, name: <b>%s</b>, status: <b>%s</b>, secure: <b>%s</b>",
                zone.getId(),
                zone.getName(),
                zone.getStatus().toString(),
                zone.getSecure() ? "YES" : "NO");    }
}
