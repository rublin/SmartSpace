package org.rublin.service;

import org.rublin.model.Camera;
import org.rublin.model.Zone;
import org.rublin.util.exception.NotFoundException;

import java.util.Collection;

/**
 * Created by Ruslan Sheremet (rublin) on 10.09.2016.
 */
public interface CameraService {
    Camera save (Camera camera, Zone zone);
    void delete(int id) throws NotFoundException;
    Camera get(int id) throws NotFoundException;
    Collection<Camera> getAll();
    Collection<Camera> getByZone(Zone zone);
}
