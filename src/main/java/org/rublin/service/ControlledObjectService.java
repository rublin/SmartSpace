package org.rublin.service;

import org.rublin.model.ControlledObject;
import org.rublin.util.exception.NotFoundException;

import java.util.Collection;

/**
 * Created by Sheremet on 11.07.2016.
 */
public interface ControlledObjectService {
    ControlledObject save(ControlledObject object);
    void delete (int id) throws NotFoundException;
    ControlledObject get(int id) throws NotFoundException;
    Collection<ControlledObject> getAll();
}
