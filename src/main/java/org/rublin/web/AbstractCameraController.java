package org.rublin.web;

import org.rublin.model.Camera;
import org.rublin.model.Zone;
import org.rublin.service.CameraService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 02.10.2016.
 */
public class AbstractCameraController {
    protected final Logger LOG = getLogger(getClass());

    @Autowired
    private CameraService cameraService;

    public List<Camera> getAll() {
        LOG.debug("get all cameras");
        return cameraService.getAll();
    }

    public Camera get(int id) {
        LOG.debug("get camera with id {}", id);
        return cameraService.get(id);
    }

    public void delete(int id) {
        LOG.info("delete camera with id {}", id);
        cameraService.delete(id);
    }

    public Camera createOrUpdate(Camera camera, Zone zone, String id) {
        if (id == null || id.equals("")) {
            LOG.info("new camera {} added in zone", camera, zone);
        } else {
            camera.setId(Integer.valueOf(id));
            LOG.info("camera {} changed in zone", camera, zone);
        }
        return cameraService.save(camera, zone);
    }
}
