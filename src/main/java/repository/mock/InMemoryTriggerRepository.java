package repository.mock;

import model.AbstractTrigger;
import model.DigitTrigger;
import org.springframework.stereotype.Repository;
import repository.TriggerRepository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sheremet on 15.06.2016.
 */
@Repository
public class InMemoryTriggerRepository implements TriggerRepository {
    Map<Integer, DigitTrigger> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    @Override
    public DigitTrigger save(DigitTrigger trigger) {
        if (trigger.isNew())
            trigger.setId(counter.incrementAndGet());
        repository.put(trigger.getId(), trigger);
        return trigger;
    }

    @Override
    public DigitTrigger delete(int id) {
        return repository.remove(id);
    }

    @Override
    public DigitTrigger get(int id) {
        return repository.get(id);
    }

    @Override
    public Collection<DigitTrigger> getAll() {
        return repository.values();
    }
}
