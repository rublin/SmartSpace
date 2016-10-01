package org.rublin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rublin.model.user.Role;
import org.rublin.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import static org.rublin.UserTestData.*;
import static org.junit.Assert.*;

/**
 * Created by Ruslan Sheremet (rublin) on 10.09.2016.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceImplTest {

    @Autowired
    private UserService service;

    @Test
    public void testSave() throws Exception {
//        User newUser = new User("TestName", "TestLastName", Collections.singleton(Role.ROLE_USER), "user@user.ua", "P@ssw0rd", "+380441234567", "name");
//        service.save(newUser);
//        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN, USER, newUser), service.getAll());
    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testGetByEmail() throws Exception {

    }

    @Test
    public void testGetByTelegramId() throws Exception {

    }

    @Test
    public void testGetByTelegramName() throws Exception {

    }

    @Test
    public void testGetByMobile() throws Exception {

    }

    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testEnable() throws Exception {

    }
}