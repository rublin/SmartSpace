package org.rublin.repository.mock;

import org.rublin.model.Zone;
import org.rublin.service.ZoneService;
import org.rublin.util.exception.NotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Sheremet on 11.07.2016.
 */
public class InMemoryObjectRepository implements ZoneService {
    private Map<Integer, Zone> repository = new ConcurrentHashMap<>();

    {
        repository.put(10, new Zone(10, "Home"));
    }

    @Override
    public Zone save(Zone object) {
        return repository.put(object.getId(), object);
    }

    @Override
    public void delete(int id) throws NotFoundException {

    }

    @Override
    public Zone get(int id) throws NotFoundException {
        return null;
    }

    @Override
    public Collection<Zone> getAll() {
        return null;
    }
}
