package util;

import model.event.DigitEvent;
import model.DigitTrigger;
import model.event.Event;

import java.util.*;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class TriggerInit {
    public static final List<DigitTrigger> TRIGGER_LIST = Arrays.asList(
            new DigitTrigger("Move 1 floor"),
            new DigitTrigger("Move 2 floor"),
            new DigitTrigger("Door 1 floor")
    );
    public static final List<Event> EVENT_LIST = Arrays.asList(
            new DigitEvent(TRIGGER_LIST.get(1), Boolean.TRUE),
            new DigitEvent(TRIGGER_LIST.get(1), Boolean.FALSE)
    );
}
