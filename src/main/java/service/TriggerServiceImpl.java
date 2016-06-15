package service;

import model.DigitTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.TriggerRepository;
import util.exception.NotFoundException;

import java.util.Collection;

/**
 * Created by Sheremet on 15.06.2016.
 */
@Service
public class TriggerServiceImpl implements TriggerService {
    @Autowired
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
