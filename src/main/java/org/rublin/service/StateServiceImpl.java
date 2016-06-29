package org.rublin.service;

import org.rublin.model.Trigger;
import org.rublin.model.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.rublin.repository.StateRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Sheremet on 16.06.2016.
 */
@Service
public class StateServiceImpl implements StateService {
    @Autowired
    private StateRepository repository;
    @Override
    public void save(Trigger trigger, Event event) {
        repository.save(trigger, event);
    }

    @Override
    public List<Event> get(Trigger trigger) {
        return repository.get(trigger);
    }

    @Override
    public List<Event> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Event> getBetween(LocalDateTime from, LocalDateTime to) {
        return repository.getBetween(from, to);
    }
}
