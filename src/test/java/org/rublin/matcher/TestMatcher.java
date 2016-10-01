package org.rublin.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.rublin.web.json.JsonUtil;

/**
 * Created by Ruslan Sheremet (rublin) on 30.09.2016.
 */
public abstract class TestMatcher<T> extends BaseMatcher<String> {
    protected T expected;

    public TestMatcher(T expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object actual) {
        return compare(expected, (String) actual);
    }

    abstract protected boolean compare(T expected, String actual);

    @Override
    public void describeTo(Description description) {
        description.appendText(JsonUtil.writeValue(expected));
    }
}
