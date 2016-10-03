package org.rublin.web.rest;

import org.rublin.model.Zone;
import org.rublin.web.AbstractZoneController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruslan Sheremet (rublin) on 01.10.2016.
 */
@RestController
@RequestMapping(ZoneRestController.REST_URL)
public class ZoneRestController extends AbstractZoneController {
    static final String REST_URL = "/rest/zone";

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Zone> zoneList() {
        return new ArrayList<>(super.getAll());
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Zone zone, @PathVariable("id") String id) {
        super.createOrUpdate(zone, id);
    }

    @RequestMapping(value = "/admin/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Zone> create(@RequestBody Zone zone) {
        Zone created = super.createOrUpdate(zone, null);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/admin/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Zone get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @RequestMapping(value = "/{id}/secure/{sec}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void secure(@PathVariable("sec") boolean sec, @PathVariable("id") int id) {
        super.setSecure(id, sec);
    }
}
