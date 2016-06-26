package repository.mock;

import model.Trigger;
import org.springframework.stereotype.Repository;
import repository.TriggerRepository;
import util.TriggerInit;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sheremet on 15.06.2016.
 */
@Repository
public class InMemoryTriggerRepository implements TriggerRepository {
    private Map<Integer, Trigger> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    {
        TriggerInit.TRIGGER_LIST.forEach(this::save);
    }
    @Override
    public Trigger save(Trigger trigger) {
        if (trigger.isNew())
            trigger.setId(counter.incrementAndGet());
        repository.put(trigger.getId(), trigger);
        return trigger;
    }

    @Override
    public Trigger delete(int id) {
        return repository.remove(id);
    }

    @Override
    public Trigger get(int id) {
        return repository.get(id);
    }

    @Override
    public Collection<Trigger> getAll() {
        return repository.values();
    }
}
