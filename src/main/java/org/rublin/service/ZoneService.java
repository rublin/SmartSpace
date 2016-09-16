package org.rublin.service;

import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.util.exception.NotFoundException;

import java.util.Collection;

/**
 * Created by Sheremet on 11.07.2016.
 */
public interface ZoneService {
    Zone save(Zone zone);
    void delete (int id) throws NotFoundException;
    Zone get(int id) throws NotFoundException;
    Collection<Zone> getAll();
    void setSecure(Zone zone, boolean security);
    Zone setStatus(Zone zone, ZoneStatus status);
    String getInfo(Zone zone);


}
