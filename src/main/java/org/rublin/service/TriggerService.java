package org.rublin.service;

import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.rublin.util.exception.NotFoundException;

import java.util.Collection;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface TriggerService {
    Trigger save(Trigger trigger, Zone obj);
    void delete(int id) throws NotFoundException;
    Trigger get(int id) throws NotFoundException;
    Collection<Trigger> getAll();
}
