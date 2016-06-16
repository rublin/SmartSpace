package service;

import model.AbstractTrigger;
import model.DigitTrigger;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface StateService {
    void save (DigitTrigger trigger, boolean state);
    Map<LocalDateTime, Boolean> getAll(DigitTrigger trigger);
}
