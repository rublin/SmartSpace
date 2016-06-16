package web.rest;

import model.DigitTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.StateService;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by Sheremet on 16.06.2016.
 */
@Controller
public class StateRestController {
    @Autowired
    private StateService service;
    public void save (DigitTrigger trigger, boolean state) {
        service.save(trigger, state);
    }
    public Map<LocalDateTime, Boolean> getAll (DigitTrigger trigger) {
        return service.getAll(trigger);
    }

}
