package org.rublin.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.rublin.model.Relay;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.rublin.model.RelayState.OFF;
import static org.rublin.model.RelayState.ON;

@RunWith(MockitoJUnitRunner.class)
public class RelayServiceImplTest {

    @InjectMocks
    private RelayServiceImpl relayService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(relayService, "workSeconds", 600);
    }

    @Test
    public void calculateState() {
        Relay relay = new Relay();
        relay.setProductivityPerCent(80);
        relay.setUpdated(LocalDateTime.now().minusSeconds(900));

        assertEquals(ON, relayService.calculateState(relay));

        relay.setUpdated(LocalDateTime.now().minusSeconds(700));
        assertEquals(OFF, relayService.calculateState(relay));

        relay.setUpdated(LocalDateTime.now().minusSeconds(1450));
        assertEquals(OFF, relayService.calculateState(relay));

        relay.setUpdated(LocalDateTime.now().minusSeconds(1501));
        assertEquals(ON, relayService.calculateState(relay));

        relay.setProductivityPerCent(50);
        relay.setUpdated(LocalDateTime.now().minusSeconds(900));

        assertEquals(OFF, relayService.calculateState(relay));

        relay.setProductivityPerCent(0);
        assertEquals(OFF, relayService.calculateState(relay));

        relay.setUpdated(LocalDateTime.now().minusSeconds(10));
        assertEquals(OFF, relayService.calculateState(relay));
    }
}