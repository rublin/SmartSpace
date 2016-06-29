package org.rublin;

import org.rublin.matcher.ModelMatcher;
import org.rublin.model.Trigger;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by Sheremet on 29.06.2016.
 */
public class ServiceTestData {
    public static final int DIGIT_EVENT1_ID = 1002;
    public static final int ANALOG_EVENT1_ID = 1008;
    public static final Trigger DIGITAL_TRIGGER = TriggerTestData.DIGITAL_TRIGGER;
    public static final Trigger ANALOG_TRIGGER = TriggerTestData.ANALOG_TRIGGER;

    public static final DigitEvent DIGIT_EVENT1 = new DigitEvent(DIGIT_EVENT1_ID, DIGITAL_TRIGGER, true, LocalDateTime.of(2016, Month.JUNE, 20, 11, 00, 00));
    public static final DigitEvent DIGIT_EVENT2 = new DigitEvent(DIGIT_EVENT1_ID+1, DIGITAL_TRIGGER, false, LocalDateTime.of(2016, Month.MAY, 30, 11, 00, 00));
    public static final DigitEvent DIGIT_EVENT3 = new DigitEvent(DIGIT_EVENT1_ID+2, DIGITAL_TRIGGER, true, LocalDateTime.of(2016, Month.MAY, 30, 20, 00, 00));
    public static final DigitEvent DIGIT_EVENT4 = new DigitEvent(DIGIT_EVENT1_ID+3, DIGITAL_TRIGGER, false, LocalDateTime.of(2016, Month.MAY, 31, 10, 00, 00));
    public static final DigitEvent DIGIT_EVENT5 = new DigitEvent(DIGIT_EVENT1_ID+4, DIGITAL_TRIGGER, true, LocalDateTime.of(2016, Month.MAY, 31, 13, 00, 00));
    public static final DigitEvent DIGIT_EVENT6 = new DigitEvent(DIGIT_EVENT1_ID+5, DIGITAL_TRIGGER, false, LocalDateTime.of(2016, Month.MAY, 31, 20, 00, 00));
    public static final AnalogEvent ANALOG_EVENT1 = new AnalogEvent(ANALOG_EVENT1_ID, ANALOG_TRIGGER, 22.5, LocalDateTime.of(2016, Month.JUNE, 1, 14, 00, 00));
    public static final AnalogEvent ANALOG_EVENT2 = new AnalogEvent(ANALOG_EVENT1_ID+1, ANALOG_TRIGGER, 19.8, LocalDateTime.of(2016, Month.JUNE, 1, 21, 00, 00));

    public static final ModelMatcher<Event, String> MATCHER = new ModelMatcher<>(Event::toString);

    public static List<Event> events() {
        List<Event> events = new ArrayList<>();
        events.add(DIGIT_EVENT1);
        events.add(DIGIT_EVENT2);
        events.add(DIGIT_EVENT3);
        events.add(DIGIT_EVENT4);
        events.add(DIGIT_EVENT5);
        events.add(DIGIT_EVENT6);
        events.add(ANALOG_EVENT1);
        events.add(ANALOG_EVENT2);
        return events;
    }
    public static List<Event> sortedEvents() {
        //List<Event> events = events();
        //events.add(newEvent);
        return events().stream()
                .sorted((Event e1, Event e2) -> e2.getTime().compareTo(e1.getTime()))
                .collect(Collectors.toList());
    }
}
