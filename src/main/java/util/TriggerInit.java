package util;

import model.Trigger;
import model.event.DigitEvent;
import model.event.Event;

import java.util.*;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class TriggerInit {
    public static final List<Trigger> TRIGGER_LIST = Arrays.asList(
            new Trigger("Move 1 floor"),
            new Trigger("Move 2 floor"),
            new Trigger("Door 1 floor")
    );
    public static final List<Event> EVENT_LIST = Arrays.asList(
            new DigitEvent(TRIGGER_LIST.get(1), Boolean.TRUE),
            new DigitEvent(TRIGGER_LIST.get(1), Boolean.FALSE)
    );
}
