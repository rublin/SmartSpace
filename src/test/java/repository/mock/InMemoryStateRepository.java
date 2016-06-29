package repository.mock;

import model.*;
import model.event.Event;
import org.springframework.stereotype.Repository;
import repository.StateRepository;
import util.TriggerInit;

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
    {
        repository.put(1, new CopyOnWriteArrayList<>());
        TriggerInit.EVENT_LIST.forEach(event -> repository.get(1).add(event));
    }

    @Override
    public void save(Trigger trigger, Event event) {
        if (!repository.containsKey(trigger.getId()))
            repository.put(trigger.getId(), new CopyOnWriteArrayList<>());
        repository.get(trigger.getId()).add(event);
    }

    @Override
    public List<Event> get(Trigger trigger) {
        //LOG.debug("get", trigger.getId());
        if (repository.containsKey(trigger.getId()))
            return repository.get(trigger.getId());
        else
            return new ArrayList<>();
        //return repository.containsKey(trigger.getId()) ? repository.get(trigger.getId()) : new ArrayList<Event>();
    }

    @Override
    public List<Event> getAll() {
        return null;
    }
}
