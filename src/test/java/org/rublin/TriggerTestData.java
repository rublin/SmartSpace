package org.rublin;

import org.rublin.matcher.ModelMatcher;
import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.rublin.model.Type;

/**
 * Created by Sheremet on 28.06.2016.
 */
public class TriggerTestData {
    public static final Zone OBJECT = ControlledObjectTestData.OBJECT;
    public static final int DIGITAL_TRIGGER_ID = 100;
    public static final int ANALOG_TRIGGER_ID = 101;

    public static final Trigger DIGITAL_TRIGGER = new Trigger(DIGITAL_TRIGGER_ID, OBJECT, "Door 1 floor", Type.DIGITAL);
    public static final Trigger ANALOG_TRIGGER = new Trigger(ANALOG_TRIGGER_ID, OBJECT, "Temperature 2 floor", Type.ANALOG);

    public static final ModelMatcher<Trigger, String> MATCHER = new ModelMatcher<>(Trigger::toString);
}
