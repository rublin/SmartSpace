package web.rest;

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
    public DigitTrigger save (DigitTrigger trigger) {
        return service.save(trigger);
    }
    public DigitTrigger get (int id) {
        return service.get(id);
    }
    public List<DigitTrigger> getAll () {
        return new ArrayList<>(service.getAll());
    }
}
