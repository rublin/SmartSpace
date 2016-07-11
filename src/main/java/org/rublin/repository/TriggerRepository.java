package org.rublin.repository;

import org.rublin.model.ControlledObject;
import org.rublin.model.Trigger;

import java.util.Collection;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface TriggerRepository {
    Trigger save (Trigger trigger, ControlledObject obj);
    boolean delete (int id);
    Trigger get (int id);
    Collection<Trigger> getAll ();
}
