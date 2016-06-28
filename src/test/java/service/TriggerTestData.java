package service;

import matcher.ModelMatcher;
import model.Trigger;
import model.Type;

/**
 * Created by Sheremet on 28.06.2016.
 */
public class TriggerTestData {
    public static final int DIGITAL_TRIGGER_ID = 1000;
    public static final int ANALOG_TRIGGER_ID = 1001;

    public static final Trigger DIGITAL_TRIGGER = new Trigger(DIGITAL_TRIGGER_ID, "Door 1 floor", Type.DIGITAL);
    public static final Trigger ANALOG_TRIGGER = new Trigger(ANALOG_TRIGGER_ID, "Temperature 2 floor", Type.ANALOG);

    public static final ModelMatcher<Trigger, String> MATCHER = new ModelMatcher<>(Trigger::toString);
}
