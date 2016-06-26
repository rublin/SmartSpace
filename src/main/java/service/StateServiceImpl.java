package service;

import model.AbstractTrigger;
import model.Event;
import model.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.StateRepository;

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
    public List<Event> getAll(Trigger trigger) {
        return repository.get(trigger);
    }
}
