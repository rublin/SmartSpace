package repository;

import model.AbstractTrigger;
import model.DigitTrigger;
import model.DigitEvent;
import model.Event;

import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface StateRepository {
    void save (AbstractTrigger trigger, Event event);
    List<Event> getAll (AbstractTrigger trigger);
}
