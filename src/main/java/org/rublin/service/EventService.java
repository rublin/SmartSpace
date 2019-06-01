package org.rublin.service;

import org.rublin.model.Trigger;
import org.rublin.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    void save(Event event);

    List<Event> get(Trigger trigger);

    List<Event> get(Trigger trigger, int numberLatestEvents);

    List<Event> getAll();

    List<Event> getBetween(LocalDateTime from, LocalDateTime to);

    List<Event> getAlarmed();
}
