package org.rublin.web.rest;

import org.rublin.model.Camera;
import org.rublin.web.AbstractCameraController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Created by Ruslan Sheremet (rublin) on 02.10.2016.
 */
@RestController
@RequestMapping(CameraRestController.REST_URL)
public class CameraRestController extends AbstractCameraController {
    static final String REST_URL = "/rest/camera";

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Camera get (@PathVariable("id") int id) {
        return super.get(id);
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Camera> getAll() {
        return super.getAll();
    }

    @RequestMapping(value = "/admin/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Camera> create(@RequestBody Camera camera) {
        Camera created = super.createOrUpdate(camera, camera.getZone(), null);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/admin/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Camera camera, @PathVariable("id") String id) {
        super.createOrUpdate(camera, camera.getZone(), id);
    }
}
