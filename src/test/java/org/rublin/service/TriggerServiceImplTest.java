package org.rublin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.rublin.TriggerTestData.ANALOG_TRIGGER;
import static org.rublin.TriggerTestData.ANALOG_TRIGGER_ID;
import static org.rublin.TriggerTestData.DIGITAL_TRIGGER;
import static org.rublin.TriggerTestData.DIGITAL_TRIGGER_ID;
import static org.rublin.TriggerTestData.MATCHER;
import static org.rublin.TriggerTestData.TRIGGERS_INFO;
import static org.rublin.TriggerTestData.ZONE;
import static org.rublin.TriggerTestData.ZONE_ID;


/**
 * Created by Sheremet on 28.06.2016.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/migration/V2__populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class TriggerServiceImplTest {

    static {
        System.getProperties().setProperty("jna.library.path",
                "C:\\Program Files\\VideoLAN\\VLC");
    }

    @Mock
    private MediaPlayerService mediaPlayerService;

    @Autowired
    protected TriggerService service;

    @Autowired
    private ZoneService zoneService;

    @Test
    @Transactional
    public void deleteTriggersByZone() {
        Zone zone1 = createAndSaveZone("testZone1");
        Zone zone2 = createAndSaveZone("testZone2");
        assertEquals(3, zoneService.getAll().size());
        Trigger trigger1 = createAndSaveTrigger("testTrigger1", zone1);
        Trigger trigger2 = createAndSaveTrigger("testTrigger2", zone2);
        assertEquals(zone1, trigger1.getZone());
        assertEquals(zone2, trigger2.getZone());
        assertEquals(4, service.getAll().size());
        service.delete(Arrays.asList(zone1, zone2));
        assertEquals(2, service.getAll().size());
    }

    private Trigger createAndSaveTrigger(String name, Zone zone) {
        Trigger trigger = new Trigger();
        trigger.setState(true);
        trigger.setSecure(true);
        trigger.setType(Type.DIGITAL);
        trigger.setName(name);
//        trigger.setZone(zone);
        return service.save(trigger, zone);
    }

    private Zone createAndSaveZone(String zoneName) {
        Zone zone = new Zone();
        zone.setName(zoneName);
        zone.setShortName(zoneName.substring(zoneName.length() - 2));
        zone.setStatus(ZoneStatus.GREEN);
        zone.setSecure(false);
        return zoneService.save(zone);
    }

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
        assertEquals(TRIGGERS_INFO, service.getHtmlInfo(zoneService.get(ZONE_ID)));
    }
}