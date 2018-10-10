package org.rublin.telegram;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.rublin.telegram.TelegramCommand.CAMERA_ALL;
import static org.rublin.telegram.TelegramCommand.SECURITY;
import static org.rublin.telegram.TelegramCommand.SECURITY_ALL;

public class TelegramCommandTest {

    @Test
    public void getCommandName() {
        assertEquals("Security", SECURITY.getCommandName());
        assertEquals("All zones", SECURITY_ALL.getCommandName());
        assertEquals("All cameras", CAMERA_ALL.getCommandName());
    }

    @Test
    public void fromCommandName() {
       assertEquals(SECURITY,  TelegramCommand.fromCommandName("Security"));
       assertEquals(SECURITY_ALL,  TelegramCommand.fromCommandName("All zones"));
       assertEquals(CAMERA_ALL,  TelegramCommand.fromCommandName("All cameras"));
    }
}