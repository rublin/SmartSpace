package org.rublin.service;

import org.rublin.controller.ModemController;
import org.rublin.controller.TelegramController;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.repository.ZoneRepository;
import org.rublin.controller.Notification;
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
    private Notification notification;

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
        notification.sendInfoToAllUsers(zone);
    }

    @Override
    public Zone setStatus(Zone zone, ZoneStatus status) {
        zone.setStatus(status);
        zoneRepository.save(zone);
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
        LOG.info("Notification sending");
        notification.sendAlarmNotification(zone);
    }


}
