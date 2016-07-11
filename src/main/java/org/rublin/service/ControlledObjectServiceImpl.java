package org.rublin.service;

import org.rublin.model.ControlledObject;
import org.rublin.repository.ControlledObjectRepository;
import org.rublin.util.exception.ExceptionUtil;
import org.rublin.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Sheremet on 11.07.2016.
 */
@Service
public class ControlledObjectServiceImpl implements ControlledObjectService {

    @Autowired
    private ControlledObjectRepository repository;

    @Override
    public ControlledObject save(ControlledObject object) {
        return repository.save(object);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public ControlledObject get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public Collection<ControlledObject> getAll() {
        return repository.getAll();
    }
}
