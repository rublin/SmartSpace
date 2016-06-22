package service;

import model.AbstractTrigger;
import model.DigitTrigger;
import util.exception.NotFoundException;

import java.util.Collection;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface TriggerService {
    AbstractTrigger save (AbstractTrigger trigger);
    AbstractTrigger delete (int id) throws NotFoundException;
    AbstractTrigger get (int id) throws NotFoundException;
    Collection<AbstractTrigger> getAll ();
}
