package org.rublin.repository.mock;

import org.rublin.model.ControlledObject;
import org.rublin.service.ControlledObjectService;
import org.rublin.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Sheremet on 11.07.2016.
 */
public class InMemoryObjectRepository implements ControlledObjectService{
    private Map<Integer, ControlledObject> repository = new ConcurrentHashMap<>();

    {
        repository.put(10, new ControlledObject(10, "Home"));
    }

    @Override
    public ControlledObject save(ControlledObject object) {
        return repository.put(object.getId(), object);
    }

    @Override
    public void delete(int id) throws NotFoundException {

    }

    @Override
    public ControlledObject get(int id) throws NotFoundException {
        return null;
    }

    @Override
    public Collection<ControlledObject> getAll() {
        return null;
    }
}
