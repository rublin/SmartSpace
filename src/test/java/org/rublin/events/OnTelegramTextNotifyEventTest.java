package org.rublin.events;

import org.junit.Test;

public class OnTelegramTextNotifyEventTest {

    @Test(expected = IllegalArgumentException.class)
    public void validationNullTest() {
        new OnTelegramTextNotifyEvent(null, null);
    }
}