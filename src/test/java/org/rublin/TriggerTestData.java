package org.rublin;

import org.rublin.matcher.ModelMatcher;
import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.rublin.model.Type;

/**
 * Created by Sheremet on 28.06.2016.
 */
public class TriggerTestData {
    public static final Zone ZONE = ZoneTestData.ZONE;
    public static final int ZONE_ID = ZoneTestData.ZONE_ID;
    public static final int DIGITAL_TRIGGER_ID = 103;
    public static final int ANALOG_TRIGGER_ID = 104;
    public static final String TRIGGERS_INFO = "<h2>Triggers</h2>\n" +
            "<table >\n" +
            "    <thead>\n" +
            "    <tr>\n" +
            "        <th>Name</th>\n" +
            "        <th>State</th>\n" +
            "    </tr>\n" +
            "    </thead>\n" +
            "<tr style=\"color: green\">        <td>Door 1 floor</td><td>GOOD</td></tr><tr style=\"color: green\">        <td>Temperature 2 floor</td><td>GOOD</td></tr>";


    public static final Trigger DIGITAL_TRIGGER = new Trigger(DIGITAL_TRIGGER_ID, ZONE, "Door 1 floor", Type.DIGITAL, true);
    public static final Trigger ANALOG_TRIGGER = new Trigger(ANALOG_TRIGGER_ID, ZONE, "Temperature 2 floor", Type.ANALOG, false, 15.0, 25.0);

    public static final ModelMatcher<Trigger> MATCHER = new ModelMatcher<>(Trigger.class);
}
