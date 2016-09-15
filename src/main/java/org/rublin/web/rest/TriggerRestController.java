package org.rublin.web.rest;

import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.rublin.service.TriggerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
//@Controller
public class TriggerRestController {
    private static final Logger LOG = LoggerFactory.getLogger(TriggerRestController.class);

    @Autowired
    private TriggerService service;

    public void delete (int id){
        LOG.info("delete trigger id: {}", id);
        service.delete(id);
    }
    public Trigger create (Trigger trigger, Zone obj) {
        LOG.info("create trigger {}",trigger);
        return service.save(trigger, obj);
    }
    public Trigger update(Trigger trigger, Zone obj) {
        LOG.info("update trigger {}", trigger);
        return service.save(trigger, obj);
    }
    public Trigger get (int id) {
        LOG.info("get trigger id: {}",id);
        return service.get(id);
    }
    @RequestMapping(value = "/triggers", method = RequestMethod.GET)
    public List<Trigger> getAll () {
        LOG.info("get all triggers");
        return new ArrayList<>(service.getAll());
    }
}
