package org.rublin.service;

import org.rublin.model.Camera;
import org.rublin.model.Zone;
import org.rublin.repository.CameraRepository;
import org.rublin.util.exception.ExceptionUtil;
import org.rublin.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created by Ruslan Sheremet (rublin) on 10.09.2016.
 */
@Service
public class CameraServiceImpl implements CameraService {

    @Autowired
    private CameraRepository repository;

    @Override
    public Camera save(Camera camera, Zone zone) {
        return repository.save(camera, zone);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public Camera get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public List<Camera> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Camera> getByZone(Zone zone) {
        return repository.getByZone(zone);
    }
}
