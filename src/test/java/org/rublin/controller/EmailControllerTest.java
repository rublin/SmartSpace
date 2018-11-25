package org.rublin.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.Assert.*;

public class EmailControllerTest {
    public static final String LOGIN = "";
    public static final String PASSWORD = "";
    private static final Object SMTP = "smtp.ukr.net";
    private static final Object PORT = "465";

    EmailController emailController = new EmailController();

    @Before
    public void init() {
        ReflectionTestUtils.setField(emailController, "smtp", SMTP);
        ReflectionTestUtils.setField(emailController, "port", PORT);
        ReflectionTestUtils.setField(emailController, "login", LOGIN);
        ReflectionTestUtils.setField(emailController, "password", PASSWORD);
        ReflectionTestUtils.setField(emailController, "from", LOGIN);
    }

    @Test
    public void sendMail() {
        emailController.sendMail("SMS received", "Код доступу в Інтернет-Помічник: 30986178. Не повідомляйте його сто", Collections.singletonList("toor.ua@gmail.com"));
    }
}