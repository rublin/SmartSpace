package service;

import model.DigitTrigger;
import util.exception.NotFoundException;

import java.util.Collection;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface TriggerService {
    DigitTrigger save (DigitTrigger trigger);
    DigitTrigger delete (int id) throws NotFoundException;
    DigitTrigger get (int id) throws NotFoundException;
    Collection<DigitTrigger> getAll ();
}
