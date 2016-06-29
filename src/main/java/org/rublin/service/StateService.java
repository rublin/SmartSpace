package org.rublin.service;

import org.rublin.model.Trigger;
import org.rublin.model.event.Event;

import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface StateService {
    void save (Trigger trigger, Event event);
    List<Event> getAll(Trigger trigger);
}
