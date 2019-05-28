package org.rublin.service.impl;

import org.junit.Before;
import org.junit.Ignore;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.rublin.EventTestData.ANALOG_TRIGGER;
import static org.rublin.EventTestData.DIGITAL_TRIGGER;

@RunWith(MockitoJUnitRunner.class)
public class ZoneServiceImplTest {

    private static final LocalDateTime FRIDAY_6_00_MORNING = LocalDateTime.of(2019, 5, 24, 6, 1);

    @Mock
    private ZoneRepositoryJpa zoneRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    ZoneServiceImpl zoneService;

    @Before
    public void init() {
        ReflectionTestUtils.setField(zoneService, "threshold", 30);
        ReflectionTestUtils.setField(zoneService, "nightPeriodWorkdays", new String[]{"22:00", "05:00"});
        ReflectionTestUtils.setField(zoneService, "nightPeriodWeekends", new String[]{"22:00", "07:00"});
    }

    @Test
    @Ignore
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

    @Test
    public void nightTimeTest() {
        assertTrue(zoneService.nightTime(LocalDateTime.of(FRIDAY_6_00_MORNING.toLocalDate(), LocalTime.of(23, 0))));
        assertTrue(zoneService.nightTime(LocalDateTime.of(FRIDAY_6_00_MORNING.toLocalDate(), LocalTime.of(22, 1))));
        assertTrue(zoneService.nightTime(LocalDateTime.of(FRIDAY_6_00_MORNING.toLocalDate(), LocalTime.of(4, 59))));
        assertFalse(zoneService.nightTime(LocalDateTime.of(FRIDAY_6_00_MORNING.toLocalDate(), LocalTime.of(22, 0))));
        assertFalse(zoneService.nightTime(LocalDateTime.of(FRIDAY_6_00_MORNING.toLocalDate(), LocalTime.of(5, 0))));
        assertFalse(zoneService.nightTime(LocalDateTime.of(FRIDAY_6_00_MORNING.toLocalDate(), LocalTime.of(7, 0))));
        assertFalse(zoneService.nightTime(LocalDateTime.of(FRIDAY_6_00_MORNING.toLocalDate(), LocalTime.of(9, 0))));
        assertFalse(zoneService.nightTime(LocalDateTime.of(FRIDAY_6_00_MORNING.toLocalDate(), LocalTime.of(21, 0))));
    }

    @Test
    public void morningStartsTest() {
        //Work day
        assertTrue(zoneService.morningStarts(FRIDAY_6_00_MORNING));
        assertFalse(zoneService.morningStarts(FRIDAY_6_00_MORNING.minusHours(1).minusMinutes(1)));
        assertFalse(zoneService.morningStarts(FRIDAY_6_00_MORNING.plusHours(2)));

        //Weekend day
        assertFalse(zoneService.morningStarts(FRIDAY_6_00_MORNING.plusDays(1)));
        assertFalse(zoneService.morningStarts(FRIDAY_6_00_MORNING.plusDays(1).plusHours(3)));
        assertTrue(zoneService.morningStarts(FRIDAY_6_00_MORNING.plusDays(1).plusHours(1)));
    }

    @Test
    public void isWeekendTest() {
        assertFalse(zoneService.isWeekend(FRIDAY_6_00_MORNING));
        assertTrue(zoneService.isWeekend(FRIDAY_6_00_MORNING.plusDays(1)));
        assertTrue(zoneService.isWeekend(FRIDAY_6_00_MORNING.plusDays(2)));
        assertFalse(zoneService.isWeekend(FRIDAY_6_00_MORNING.plusDays(3)));
        assertFalse(zoneService.isWeekend(FRIDAY_6_00_MORNING.plusDays(4)));
        assertFalse(zoneService.isWeekend(FRIDAY_6_00_MORNING.plusDays(5)));
        assertFalse(zoneService.isWeekend(FRIDAY_6_00_MORNING.plusDays(6)));
    }
}