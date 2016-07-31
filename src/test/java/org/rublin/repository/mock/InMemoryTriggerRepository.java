package org.rublin.repository.mock;

import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.springframework.stereotype.Repository;
import org.rublin.repository.TriggerRepository;
import org.rublin.util.TriggerInit;

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
        Zone obj = new Zone(10, "Home");
        TriggerInit.TRIGGER_LIST.forEach(trigger -> save(trigger, obj));
    }
    @Override
    public Trigger save(Trigger trigger, Zone obj) {
        if (trigger.isNew())
            trigger.setId(counter.incrementAndGet());
        repository.put(trigger.getId(), trigger);
        return trigger;
    }

    @Override
    public boolean delete(int id) {
        return repository.remove(id) != null;
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
