package service;

import model.Trigger;
import util.exception.NotFoundException;

import java.util.Collection;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface TriggerService {
    Trigger save (Trigger trigger);
    Trigger delete (int id) throws NotFoundException;
    Trigger get (int id) throws NotFoundException;
    Collection<Trigger> getAll ();
}
