package org.rublin.repository;

import org.rublin.model.Camera;
import org.rublin.model.Zone;

import java.util.Collection;
import java.util.List;

/**
 * Created by Ruslan Sheremet (rublin) on 10.09.2016.
 */
public interface CameraRepository {
    Camera save(Camera camera, Zone zone);
    boolean delete(int id);
    Camera get(int id);
    List<Camera> getAll();
    List<Camera> getByZone(Zone zone);
}
