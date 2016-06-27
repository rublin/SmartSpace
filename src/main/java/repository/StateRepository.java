package repository;

import model.*;
import model.event.Event;

import java.util.List;
import java.util.Map;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface StateRepository {
    void save (Trigger trigger, Event event);
    List<Event> get(Trigger trigger);
    List<Event> getAll();
}
