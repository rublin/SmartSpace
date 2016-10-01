package org.rublin.web;

import org.rublin.model.Zone;
import org.rublin.service.ZoneService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 01.10.2016.
 */
public abstract class AbstractZoneController {
    protected final Logger LOG = getLogger(getClass());

    @Autowired
    private ZoneService zoneService;

    public Collection<Zone> getAll() {
        LOG.debug("requested all zones");
        return zoneService.getAll();
    }

    public void delete(int id) {
        LOG.info("Zone with id: {} deleted", id);
        zoneService.delete(id);
    }

    public Zone get(int id) {
        LOG.debug("Zone with id: {} selected", id);
        return zoneService.get(id);
    }

    public void setSecure(int id, boolean secure) {
        zoneService.setSecure(zoneService.get(id), secure);
        LOG.info("zone {} is secured to {}", id, secure);
    }

    public Zone createOrUpdate(Zone zone, String id) {
        if (id == null || id.equals("")) {
            LOG.info("new zone {} added", zone);
        } else {
            zone.setId(Integer.valueOf(id));
            LOG.info("zone {} changed", zone);
        }
        return zoneService.save(zone);
    }
}
