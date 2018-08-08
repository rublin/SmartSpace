package org.rublin.repository;

import org.rublin.model.*;
import org.rublin.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface EventRepository {
    void save (Trigger trigger, Event event);
    List<Event> get(Trigger trigger);
    List<Event> get(Trigger trigger, int maxRows);
    List<Event> getAll();
    List<Event> getBetween(LocalDateTime from, LocalDateTime to);
    List<Event> getAlarmed();
}
