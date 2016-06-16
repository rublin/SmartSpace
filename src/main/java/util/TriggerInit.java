package util;

import model.DigitTrigger;

import java.time.LocalDateTime;
import java.time.Month;
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
    public static final Map<LocalDateTime, Boolean> STAGE_LIST;
    static
    {
        STAGE_LIST = new ConcurrentHashMap<LocalDateTime, Boolean>();
        STAGE_LIST.put(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), Boolean.TRUE);
        STAGE_LIST.put(LocalDateTime.of(2016, Month.NOVEMBER, 01, 10, 0), Boolean.FALSE);
    }
}
