package org.rublin.service;

import static org.rublin.TriggerTestData.*;

import org.junit.Assert;
import org.rublin.model.Trigger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.rublin.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


/**
 * Created by Sheremet on 28.06.2016.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class TriggerServiceImplTest {

    @Autowired
    protected TriggerService service;

    @Autowired
    private ZoneService zoneService;

    @Test
    public void testSave() throws Exception {
        Trigger trigger = new Trigger("new");
        service.save(trigger, ZONE);
        MATCHER.assertCollectionEquals(Arrays.asList(DIGITAL_TRIGGER, trigger, ANALOG_TRIGGER), service.getAll());
    }

    @Test(expected = DataAccessException.class)
    public void testDuplicateNameSave() throws Exception {
        service.save(new Trigger("Door 1 floor"), ZONE);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(ANALOG_TRIGGER_ID);
        MATCHER.assertCollectionEquals(Collections.singletonList(DIGITAL_TRIGGER), service.getAll());
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        service.delete(1);
    }
    @Test
    public void testGet() throws Exception {
        Trigger trigger = service.get(DIGITAL_TRIGGER_ID);
        MATCHER.assertEquals(DIGITAL_TRIGGER, trigger);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        service.get(1);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Trigger> all = service.getAll();
        MATCHER.assertCollectionEquals(Arrays.asList(DIGITAL_TRIGGER, ANALOG_TRIGGER), all);
    }

    @Test
    public void testGetInfo() throws Exception {
        Assert.assertEquals(TRIGGERS_INFO, service.getHtmlInfo(zoneService.get(ZONE_ID)));
    }
}