package org.rublin.service;

import org.rublin.model.Trigger;
import org.rublin.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface EventService {
    void save (Trigger trigger, Event event);
    List<Event> get(Trigger trigger);
    List<Event> getAll();
    List<Event> getBetween(LocalDateTime from, LocalDateTime to);
}
