package service;

import model.DigitTrigger;
import repository.TriggerRepository;
import util.exceptions.NotFoundException;

import java.util.Collection;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class TriggerServiceImpl implements TriggerService {
    private TriggerRepository repository;
    @Override
    public DigitTrigger save(DigitTrigger trigger) {
        return repository.save(trigger);
    }

    @Override
    public DigitTrigger delete(int id) throws NotFoundException {
        return repository.delete(id);
    }

    @Override
    public DigitTrigger get(int id) throws NotFoundException {
        return repository.get(id);
    }

    @Override
    public Collection<DigitTrigger> getAll() {
        return repository.getAll();
    }
}
