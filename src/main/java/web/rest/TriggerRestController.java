package web.rest;

import model.AbstractTrigger;
import model.DigitTrigger;
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
    @Autowired
    private TriggerService service;
    public void delete (int id){
        service.delete(id);
    }
    public AbstractTrigger save (AbstractTrigger trigger) {
        return service.save(trigger);
    }
    public AbstractTrigger get (int id) {
        return service.get(id);
    }
    public List<AbstractTrigger> getAll () {
        return new ArrayList<>(service.getAll());
    }
}
