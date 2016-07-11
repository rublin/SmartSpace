package org.rublin;

import org.rublin.matcher.ModelMatcher;
import org.rublin.model.ControlledObject;
import org.rublin.model.ObjectSecure;
import org.rublin.model.ObjectStatus;

/**
 * Created by Sheremet on 11.07.2016.
 */
public class ControlledObjectTestData {
    public static final int OBJECT_ID = 10;
    public static final ControlledObject OBJECT = new ControlledObject(OBJECT_ID, "Home", ObjectStatus.GREEN, ObjectSecure.NOT_PROTECTED);

    public static final ModelMatcher<ControlledObject, String> MATCHER = new ModelMatcher<>(ControlledObject::toString);
}
