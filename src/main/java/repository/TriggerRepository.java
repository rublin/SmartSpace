package repository;

import model.AbstractTrigger;
import model.DigitTrigger;

import java.util.Collection;

/**
 * Created by Sheremet on 15.06.2016.
 */
public interface TriggerRepository {
    AbstractTrigger save (AbstractTrigger trigger);
    AbstractTrigger delete (int id);
    AbstractTrigger get (int id);
    Collection<AbstractTrigger> getAll ();
}
