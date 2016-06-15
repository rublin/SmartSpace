package repository.mock;

import model.DigitTrigger;
import repository.StateRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class InMemoryStateRepository implements StateRepository {
    private Map<DigitTrigger, Map<LocalDateTime, String>> repository = new ConcurrentHashMap<>();

    @Override
    public void save(DigitTrigger trigger, boolean state) {
        if (trigger.isState()!=state){
            if (!repository.containsKey(trigger))
                repository.put(trigger, new ConcurrentHashMap<>());
            repository.get(trigger).put(LocalDateTime.now(), String.valueOf(state));
            trigger.setState(state);
        }
    }

    @Override
    public Map<LocalDateTime, String> getAll(DigitTrigger trigger) {
        return repository.get(trigger);
    }
}
