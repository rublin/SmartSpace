package repository.mock;

import model.AbstractTrigger;
import model.DigitTrigger;
import model.DigitEvent;
import model.Event;
import org.springframework.stereotype.Repository;
import repository.StateRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 15.06.2016.
 */
@Repository
public class InMemoryStateRepository implements StateRepository {
    private Map<Integer, List<Event>> repository = new ConcurrentHashMap<>();
    {
        repository.put(1, Arrays.asList(
                new DigitEvent(true),
                new DigitEvent(false),
                new DigitEvent(true)
        ));
    }

    @Override
    public void save(AbstractTrigger trigger, Event event) {
        if (trigger.getEvent().getState()!= event.getState()){
            if (!repository.containsKey(trigger))
                repository.put(trigger.getId(), new ArrayList<>());
            repository.get(trigger).add(event);
            trigger.setEvent(event);
        }
    }

    @Override
    public List<Event> getAll(AbstractTrigger trigger) {
        return repository.get(trigger.getId());
    }


}
