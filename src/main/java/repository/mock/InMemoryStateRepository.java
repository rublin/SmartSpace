package repository.mock;

import model.AbstractTrigger;
import model.DigitTrigger;
import model.DigitEvent;
import model.Event;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import repository.StateRepository;
import util.TriggerInit;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 15.06.2016.
 */
@Repository
public class InMemoryStateRepository implements StateRepository {
    //private static final Logger LOG = getLogger(InMemoryStateRepository.class);
    private Map<Integer, List<Event>> repository = new ConcurrentHashMap<>();
    /*{
        repository.put(1, new CopyOnWriteArrayList<>());
        TriggerInit.EVENT_LIST.forEach(event -> repository.get(1).add(event));
    }*/

    @Override
    public void save(AbstractTrigger trigger, Event event) {
        if (trigger.getEvent().getState()!= event.getState()){
            if (!repository.containsKey(trigger.getId()))
                repository.put(trigger.getId(), new CopyOnWriteArrayList<>());
            repository.get(trigger.getId()).add(event);
            trigger.setEvent(event);
        }
    }

    @Override
    public List<Event> get(AbstractTrigger trigger) {
        //LOG.debug("get", trigger.getId());
        if (repository.containsKey(trigger.getId()))
            return repository.get(trigger.getId());
        else
            return new ArrayList<>();
        //return repository.containsKey(trigger.getId()) ? repository.get(trigger.getId()) : new ArrayList<Event>();
    }

    @Override
    public Map<Integer, List<Event>> getAll() {
        return repository;
    }
}
