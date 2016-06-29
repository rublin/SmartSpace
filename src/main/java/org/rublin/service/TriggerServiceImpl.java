package org.rublin.service;

import org.rublin.model.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.rublin.repository.TriggerRepository;
import org.rublin.util.exception.NotFoundException;

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
    public boolean delete(int id) throws NotFoundException {
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
