package org.rublin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rublin.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.rublin.ControlledObjectTestData.*;

/**
 * Created by Sheremet on 11.07.2016.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class ZoneServiceImplTest {

    @Autowired
    private ZoneService service;

    @Test
    public void testSave() throws Exception {
        Zone newObj = new Zone("new");
        service.save(newObj);
        MATCHER.assertCollectionEquals(Arrays.asList(OBJECT, newObj), service.getAll());
    }

    @Test
    public void testDelete() throws Exception {
        Zone newObj = new Zone("new");
        service.save(newObj);
        MATCHER.assertCollectionEquals(Arrays.asList(OBJECT, newObj), service.getAll());
        service.delete(OBJECT.getId());
        MATCHER.assertCollectionEquals(Collections.singletonList(newObj) , service.getAll());
    }

    @Test
    public void testGet() throws Exception {
        MATCHER.assertEquals(OBJECT, service.get(OBJECT_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        MATCHER.assertCollectionEquals(Collections.singletonList(OBJECT), service.getAll());
    }
}