package org.rublin.web;

import org.rublin.model.Trigger;
import org.rublin.model.Zone;
import org.rublin.service.TriggerService;
import org.rublin.service.ZoneService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 01.10.2016.
 */
public abstract class AbstractTriggerController {
    protected final Logger LOG = getLogger(getClass());

    @Autowired
    private TriggerService triggerService;

    public List<Trigger> getAll() {
        LOG.debug("get all triggers");
        return (List<Trigger>) triggerService.getAll();
    }

    public void delete(int id) {
        LOG.info("trigger with id {} deleted", id);
        triggerService.delete(id);
    }

    public Trigger get(int id) {
        LOG.debug("get trigger with {} id", id);
        return triggerService.get(id);
    }

    public List<Trigger> getByState(boolean state) {
        LOG.debug("get by state: {}", state);
        return triggerService.getByState(state);
    }

    public Trigger createOrUpdate(Trigger trigger, Zone zone, String id) {
        if (id == null || id.equals("")) {
            LOG.info("new trigger {} added in zone {}", trigger, zone.getName());
        } else {
            trigger.setId(Integer.valueOf(id));
            LOG.info("trigger {} changed in zone {}", trigger, zone.getName());
        }
        return triggerService.save(trigger, zone);
    }
}
