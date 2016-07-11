package org.rublin.repository;

import org.rublin.model.ControlledObject;

import java.util.Collection;
import java.util.List;

/**
 * Created by Sheremet on 11.07.2016.
 */
public interface ControlledObjectRepository {
    ControlledObject save (ControlledObject object);
    ControlledObject get(int id);
    Collection<ControlledObject> getAll();
    boolean delete(int id);
}
