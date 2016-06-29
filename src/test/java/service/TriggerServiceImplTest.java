package service;

import model.Trigger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.exception.NotFoundException;

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

    @Test
    public void testSave() throws Exception {
        Trigger trigger = new Trigger("new");
        service.save(trigger);
        TriggerTestData.MATCHER.assertCollectionEquals(Arrays.asList(TriggerTestData.DIGITAL_TRIGGER, trigger, TriggerTestData.ANALOG_TRIGGER), service.getAll());
    }

    @Test(expected = DataAccessException.class)
    public void testDuplicateNameSave() throws Exception {
        service.save(new Trigger("Door 1 floor"));
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(TriggerTestData.ANALOG_TRIGGER_ID);
        TriggerTestData.MATCHER.assertCollectionEquals(Collections.singletonList(TriggerTestData.DIGITAL_TRIGGER), service.getAll());
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        service.delete(1);
    }
    @Test
    public void testGet() throws Exception {
        Trigger trigger = service.get(TriggerTestData.DIGITAL_TRIGGER_ID);
        TriggerTestData.MATCHER.assertEquals(TriggerTestData.DIGITAL_TRIGGER, trigger);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        service.get(1);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Trigger> all = service.getAll();
        TriggerTestData.MATCHER.assertCollectionEquals(Arrays.asList(TriggerTestData.DIGITAL_TRIGGER, TriggerTestData.ANALOG_TRIGGER), all);
    }
}