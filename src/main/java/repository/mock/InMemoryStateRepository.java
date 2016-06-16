package repository.mock;

import model.AbstractTrigger;
import model.DigitTrigger;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import repository.StateRepository;
import util.TriggerInit;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 15.06.2016.
 */
@Repository
public class InMemoryStateRepository implements StateRepository {
    private Map<Integer, Map<LocalDateTime, Boolean>> repository = new ConcurrentHashMap<>();
    {
        repository.put(1, new ConcurrentHashMap<LocalDateTime, Boolean>());
        repository.get(1).put(LocalDateTime.now(), Boolean.TRUE);
        repository.get(1).put(LocalDateTime.now(), Boolean.FALSE);
        repository.get(1).put(LocalDateTime.now(), Boolean.TRUE);
    }
    @Override
    public void save(DigitTrigger trigger, boolean state) {
        if (trigger.isState()!=state){
            if (!repository.containsKey(trigger))
                repository.put(trigger.getId(), new ConcurrentHashMap<>());
            repository.get(trigger).put(LocalDateTime.now(), trigger.isState());
            trigger.setState(state);
        }
    }

    @Override
    public Map<LocalDateTime, Boolean> getAll(DigitTrigger trigger) {
        return repository.get(trigger.getId());
    }
}
