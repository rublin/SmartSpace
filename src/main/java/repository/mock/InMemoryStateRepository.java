package repository.mock;

import model.DigitTrigger;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import repository.StateRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 15.06.2016.
 */
@Repository
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
