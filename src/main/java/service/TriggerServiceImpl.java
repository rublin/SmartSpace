package service;

import model.AbstractTrigger;
import model.DigitTrigger;
import model.Trigger;
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
    public Trigger save(Trigger trigger) {
        return repository.save(trigger);
    }

    @Override
    public Trigger delete(int id) throws NotFoundException {
        return repository.delete(id);
    }

    @Override
    public Trigger get(int id) throws NotFoundException {
        return repository.get(id);
    }

    @Override
    public Collection<Trigger> getAll() {
        return repository.getAll();
    }
}
