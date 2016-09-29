package org.rublin;

import org.rublin.matcher.ModelMatcher;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;

/**
 * Created by Sheremet on 11.07.2016.
 */
public class ZoneTestData {
    public static final int ZONE_ID = 10;
    public static final Zone ZONE = new Zone(ZONE_ID, "Home", "h1", ZoneStatus.GREEN, false);
    public static final Zone ZONE_SECURE = new Zone(ZONE_ID, "Home", "h1", ZoneStatus.GREEN, true);
    public static final Zone ZONE_YELLOW = new Zone(ZONE_ID, "Home", "h1", ZoneStatus.YELLOW, false);
    public static final String INFO = "id: <b>10</b>, name: <b>Home</b>, status: <b>GREEN</b>, secure: <b>NO</b>";

    public static final ModelMatcher<Zone, String> MATCHER = new ModelMatcher<>(Zone::toString);
}
