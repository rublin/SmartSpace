package repository;

import model.DigitTrigger;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface StateRepository {
    void save (DigitTrigger trigger, boolean state);
    Map<LocalDateTime, String> getAll (DigitTrigger trigger);
}
