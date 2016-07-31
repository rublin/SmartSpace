package org.rublin;

import org.rublin.matcher.ModelMatcher;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;

/**
 * Created by Sheremet on 11.07.2016.
 */
public class ControlledObjectTestData {
    public static final int OBJECT_ID = 10;
    public static final Zone OBJECT = new Zone(OBJECT_ID, "Home", ZoneStatus.GREEN, false);

    public static final ModelMatcher<Zone, String> MATCHER = new ModelMatcher<>(Zone::toString);
}
