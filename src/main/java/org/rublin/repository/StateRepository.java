package org.rublin.repository;

import org.rublin.model.*;
import org.rublin.model.event.Event;

import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface StateRepository {
    void save (Trigger trigger, Event event);
    List<Event> get(Trigger trigger);
    List<Event> getAll();
}
