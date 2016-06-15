package repository;

import model.DigitTrigger;

import java.util.Collection;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface TriggerRepository {
    DigitTrigger save (DigitTrigger trigger);
    DigitTrigger delete (int id);
    DigitTrigger get (int id);
    Collection<DigitTrigger> getAll ();
}
