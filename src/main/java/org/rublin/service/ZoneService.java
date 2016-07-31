package org.rublin.service;

import org.rublin.model.Zone;
import org.rublin.util.exception.NotFoundException;

import java.util.Collection;

/**
 * Created by Sheremet on 11.07.2016.
 */
public interface ZoneService {
    Zone save(Zone object);
    void delete (int id) throws NotFoundException;
    Zone get(int id) throws NotFoundException;
    Collection<Zone> getAll();
}
