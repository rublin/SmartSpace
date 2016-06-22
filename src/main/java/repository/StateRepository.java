package repository;

import model.AbstractTrigger;
import model.DigitTrigger;
import model.DigitEvent;
import model.Event;

import java.util.List;
import java.util.Map;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface StateRepository {
    void save (AbstractTrigger trigger, Event event);
    List<Event> get(AbstractTrigger trigger);
    Map<Integer, List<Event>> getAll();
}
