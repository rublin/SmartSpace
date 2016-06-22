package util;

import model.AbstractTrigger;
import model.DigitEvent;
import model.DigitTrigger;
import model.Event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
            new DigitEvent(Boolean.TRUE),
            new DigitEvent(Boolean.FALSE)
    );
}
