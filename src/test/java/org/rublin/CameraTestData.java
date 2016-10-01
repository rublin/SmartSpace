package org.rublin;

import org.rublin.matcher.ModelMatcher;
import org.rublin.model.Camera;
import org.rublin.model.Zone;

/**
 * Created by Ruslan Sheremet (rublin) on 10.09.2016.
 */
public class CameraTestData {
    public static final Zone ZONE = ZoneTestData.ZONE;
    public static final int CAM_ID_102 = 102;
    public static final Camera CAM_102 = new Camera(CAM_ID_102, "Cam 1 floor 1", "192.168.0.31", "imagesaver", "QAZxsw123", "http://192.168.0.31/Streaming/channels/1/picture", ZONE);
    public static final int CAM_ID_105 = 105;
    public static final Camera CAM_105 = new Camera(CAM_ID_105, "Cam 105", "192.168.0.35", "imagesaver", "QAZxsw123", "http://192.168.0.35/Streaming/channels/1/picture", ZONE);

    public static final ModelMatcher<Camera> MATCHER = new ModelMatcher<>(Camera.class);
}
