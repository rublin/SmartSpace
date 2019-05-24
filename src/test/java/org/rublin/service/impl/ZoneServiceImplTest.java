package org.rublin.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.rublin.repository.ZoneRepositoryJpa;
import org.rublin.service.EventService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.rublin.EventTestData.ANALOG_TRIGGER;
import static org.rublin.EventTestData.DIGITAL_TRIGGER;

@RunWith(MockitoJUnitRunner.class)
public class ZoneServiceImplTest {

    @Mock
    private ZoneRepositoryJpa zoneRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    ZoneServiceImpl zoneService;

    @Before
    public void init() {
        ReflectionTestUtils.setField(zoneService, "threshold", 30);
    }

    @Test
    public void activity() {
        List<Event> events = Arrays.asList(new DigitEvent(DIGITAL_TRIGGER, true),
                new AnalogEvent(ANALOG_TRIGGER, 1.2),
                new DigitEvent(DIGITAL_TRIGGER, false),
                new AnalogEvent(ANALOG_TRIGGER, 1.2),
                new DigitEvent(DIGITAL_TRIGGER, true),
                new AnalogEvent(ANALOG_TRIGGER, 1.2),
                new DigitEvent(DIGITAL_TRIGGER, false));
        when(eventService.getBetween(any(), any())).thenReturn(events);
        zoneService.activity();
        verify(zoneService).checkZoneActivity(7);
    }
}