package org.rublin.web.rest;

import org.rublin.model.user.User;
import org.rublin.web.AbstractUserController;
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
@RequestMapping(UserRestCotroller.REST_ULR)
public class UserRestCotroller extends AbstractUserController {
    static final String REST_ULR = "/rest/admin/user";

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User get (@PathVariable("id") int id) {
        return super.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete (@PathVariable("id") int id) {
        super.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody User user, @PathVariable("id") String id) {
        super.createOrUpdate(user, id);
    }

    @RequestMapping(value = "/enable/{id}", method = RequestMethod.PUT)
    public void enable(@PathVariable("id") int id) {
        super.enable(id);
    }
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return super.getAll();
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create (@RequestBody User user) {
        User created = super.createOrUpdate(user, null);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_ULR + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }
}
