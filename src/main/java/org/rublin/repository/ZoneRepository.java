package org.rublin.repository;

import org.rublin.model.Zone;

import java.util.Collection;

/**
 * Created by Sheremet on 11.07.2016.
 */
public interface ZoneRepository {
    Zone save (Zone object);
    Zone get(int id);
    Zone getByShortName(String shortName);
    Collection<Zone> getAll();
    boolean delete(int id);
}
