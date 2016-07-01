package org.rublin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rublin.ServiceTestData;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.rublin.ServiceTestData.*;

/**
 * Created by Sheremet on 29.06.2016.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class StateServiceImplTest {

    @Autowired
    protected StateService service;

    @Test
    public void testDigitalSave() throws Exception {
        Event event = new DigitEvent(DIGITAL_TRIGGER, true);
        service.save(DIGITAL_TRIGGER, event);
        MATCHER.assertCollectionEquals(Arrays.asList(event, DIGIT_EVENT1, DIGIT_EVENT6, DIGIT_EVENT5, DIGIT_EVENT4, DIGIT_EVENT3, DIGIT_EVENT2), service.get(DIGITAL_TRIGGER));

    }
    @Test
    public void testAnalogSave() throws Exception {
        Event event = new AnalogEvent(ANALOG_TRIGGER, 29.0);
        service.save(ANALOG_TRIGGER, event);
        MATCHER.assertCollectionEquals(Arrays.asList(event, ANALOG_EVENT2, ANALOG_EVENT1), service.get(ANALOG_TRIGGER));
    }

    @Test(expected = DataAccessException.class)
    public void testDigitalDuplicateSave() throws Exception {
        service.save(DIGITAL_TRIGGER, new DigitEvent(DIGITAL_TRIGGER, false, LocalDateTime.of(2016, Month.JUNE, 20, 11, 00, 00)));
    }

    @Test(expected = DataAccessException.class)
    public void testAnalogDuplicateSave() throws Exception {
        service.save(ANALOG_TRIGGER, new AnalogEvent(ANALOG_TRIGGER, 123.189, LocalDateTime.of(2016, Month.JUNE, 1, 21, 00, 00)));
    }

    @Test
    public void testGetAll() throws Exception {
        MATCHER.assertCollectionEquals(sortedEvents(), service.getAll());
    }

    @Test
    public void testGetBetween() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(DIGIT_EVENT1, ANALOG_EVENT2, ANALOG_EVENT1),
                service.getBetween(LocalDateTime.of(2016, Month.JUNE, 01, 00,00,00), LocalDateTime.now()));
    }

    @Test
    public void testGet() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(DIGIT_EVENT1, DIGIT_EVENT6, DIGIT_EVENT5, DIGIT_EVENT4, DIGIT_EVENT3, DIGIT_EVENT2), service.get(DIGITAL_TRIGGER));
    }
}