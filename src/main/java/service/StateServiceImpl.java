package service;

import model.AbstractTrigger;
import model.DigitTrigger;
import model.DigitEvent;
import model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.StateRepository;
import repository.mock.InMemoryStateRepository;

import java.util.List;

/**
 * Created by Sheremet on 16.06.2016.
 */
@Service
public class StateServiceImpl implements StateService {
    @Autowired
    private StateRepository repository;
    @Override
    public void save(AbstractTrigger trigger, Event event) {
        repository.save(trigger, event);
    }

    @Override
    public List<Event> getAll(AbstractTrigger trigger) {
        return repository.getAll(trigger);
    }
}
