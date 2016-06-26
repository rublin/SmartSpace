package web.rest;

import model.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.TriggerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
@Controller
public class TriggerRestController {
    private static final Logger LOG = LoggerFactory.getLogger(TriggerRestController.class);
    @Autowired
    private TriggerService service;
    public void delete (int id){
        LOG.info("delete trigger id: {}", id);
        service.delete(id);
    }
    public Trigger create (Trigger trigger) {
        LOG.info("create trigger {}",trigger);
        return service.save(trigger);
    }
    public Trigger update(Trigger trigger) {
        LOG.info("update trigger {}", trigger);
        return service.save(trigger);
    }
    public Trigger get (int id) {
        LOG.info("get trigger id: {}",id);
        return service.get(id);
    }
    public List<Trigger> getAll () {
        LOG.info("get all triggers");
        return new ArrayList<>(service.getAll());
    }
}
