package org.rublin.service;

import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.rublin.util.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface TriggerService {
    Trigger save(Trigger trigger, Zone obj);
    void delete(int id) throws NotFoundException;
    Trigger get(int id) throws NotFoundException;
    List<Trigger> getByState(boolean state);
    Collection<Trigger> getAll();
    String getHtmlInfo(Zone zone);
    String getInfo(Zone zone);
}
