package org.rublin.service;

import org.rublin.model.Zone;
import org.rublin.repository.ZoneRepository;
import org.rublin.util.exception.ExceptionUtil;
import org.rublin.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Sheremet on 11.07.2016.
 */
@Service
public class ZoneServiceImpl implements ZoneService {

    @Autowired
    private ZoneRepository repository;

    @Override
    public Zone save(Zone object) {
        return repository.save(object);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public Zone get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public Collection<Zone> getAll() {
        return repository.getAll();
    }
}
