package org.rublin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rublin.model.Camera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.rublin.CameraTestData.*;
/**
 * Created by Ruslan Sheremet (rublin) on 10.09.2016.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class CameraServiceImplTest {

    @Autowired
    private CameraService service;

    @Test
    public void testSave() throws Exception {
//        Camera camera = new Camera(CAM_ID_105, ZONE);
        service.save(CAM_105, ZONE);
        MATCHER.assertCollectionEquals(Arrays.asList(CAM_102, CAM_105), service.getAll());
    }

    /*@Test(expected = DataAccessException.class)
    public void testDuplicationNameSave() throws Exception {
        service.save(new Camera("Cam 1 floor 1", "192.168.0.31", "imagesaver", "QAZxsw123", "http://192.168.0.31/Streaming/channels/1/picture", ZONE), ZONE);
    }*/

    @Test
    public void testDelete() throws Exception {
        service.save(CAM_105, ZONE);
        service.delete(CAM_ID_102);
        MATCHER.assertCollectionEquals(Collections.singletonList(CAM_105), service.getAll());
    }

    @Test
    public void testGet() throws Exception {
        Camera camera = service.get(CAM_ID_102);
        MATCHER.assertEquals(CAM_102, camera);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Camera> all = service.getAll();
        MATCHER.assertCollectionEquals(Arrays.asList(CAM_102), all);
    }

    @Test
    public void testGetByZone() throws Exception {
        Collection<Camera> allInZone = service.getByZone(ZONE);
        MATCHER.assertCollectionEquals(Arrays.asList(CAM_102), allInZone);
    }
}