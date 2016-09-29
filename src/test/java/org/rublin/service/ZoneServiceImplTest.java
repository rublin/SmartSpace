package org.rublin.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.rublin.ZoneTestData.*;

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
        Zone newZone = new Zone("New", "new");
        service.save(newZone);
        MATCHER.assertCollectionEquals(Arrays.asList(ZONE, newZone), service.getAll());
    }

    @Test
    public void testDelete() throws Exception {
        Zone newObj = new Zone("new");
        service.save(newObj);
        MATCHER.assertCollectionEquals(Arrays.asList(ZONE, newObj), service.getAll());
        service.delete(ZONE.getId());
        MATCHER.assertCollectionEquals(Collections.singletonList(newObj) , service.getAll());
    }

    @Test
    public void testGet() throws Exception {
        MATCHER.assertEquals(ZONE, service.get(ZONE_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        MATCHER.assertCollectionEquals(Collections.singletonList(ZONE), service.getAll());
    }

    @Test
    public void testSetSecure() throws Exception {
        service.setSecure(service.get(ZONE_ID), true);
        MATCHER.assertEquals(ZONE_SECURE, service.get(ZONE_ID));
    }

    @Test
    public void testSetStatus() throws Exception {
        service.setStatus(service.get(ZONE_ID), ZoneStatus.YELLOW);
        MATCHER.assertEquals(ZONE_YELLOW, service.get(ZONE_ID));
    }

    @Test
    public void testGetInfo() throws Exception {
        Assert.assertEquals(INFO, service.getInfo(service.get(ZONE_ID)));
    }

}