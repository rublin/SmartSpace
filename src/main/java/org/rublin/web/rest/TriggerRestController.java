package org.rublin.web.rest;

import org.rublin.model.Trigger;
import org.rublin.web.AbstractTriggerController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
@RestController
@RequestMapping(TriggerRestController.REST_URL)
public class TriggerRestController extends AbstractTriggerController {
    static final String REST_URL = "/rest/trigger";

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Trigger get (@PathVariable("id") int id) {
        return super.get(id);
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.DELETE)
    public void delete (@PathVariable("id") int id){
        super.delete(id);
    }

    @RequestMapping(value = "/admin/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Trigger> create (@RequestBody Trigger trigger) {
        Trigger created = super.createOrUpdate(trigger, trigger.getZone(), null);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/admin/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @RequestMapping(value = "//admin/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Trigger trigger, @PathVariable("id") String id) {
        super.createOrUpdate(trigger, trigger.getZone(), id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Trigger> getAll () {
        return new ArrayList<>(super.getAll());
    }

    @RequestMapping(value = "/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Trigger> getByState(@PathVariable("state") boolean state) {
        return super.getByState(state);
    }
}
