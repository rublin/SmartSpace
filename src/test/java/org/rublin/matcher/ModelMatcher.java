package org.rublin.matcher;

import org.junit.Assert;
import org.rublin.TestUtil;
import org.rublin.web.json.JsonUtil;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Created by Sheremet on 28.06.2016.
 */
public class ModelMatcher<T> {
    public interface EntityComparator<T> {
        boolean compare(T expected, T actual);
    }

    private static final EntityComparator DEFAULT_COMPARATOR =
            (Object expected, Object actual) -> String.valueOf(expected).equals(String.valueOf(actual));

    protected EntityComparator<T> entityComparator;
    protected Class<T> entityClass;

    public ModelMatcher(Class<T> entityClass) {
        this(entityClass, (EntityComparator<T>) DEFAULT_COMPARATOR);
    }

    public ModelMatcher(Class<T> entityClass, EntityComparator<T> entityComparator) {
        this.entityClass = entityClass;
        this.entityComparator = entityComparator;
    }

    private static class EntityWrapper<T> {
        private T entity;
        private EntityComparator<T> entityComparator;

        private EntityWrapper(T entity, EntityComparator<T> entityComparator) {
            this.entity = entity;
            this.entityComparator = entityComparator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            EntityWrapper<T> that = (EntityWrapper<T>) o;
            return entity != null ? entityComparator.compare(entity, that.entity) : that.entity == null;
        }

        @Override
        public String toString() {
            return JsonUtil.writeValue(entity);
        }
    }

    private T fromJsonValue(String json) {
        return JsonUtil.readValue(json, entityClass);
    }

    private Collection<T> fromJsonValues(String json) {
        return JsonUtil.readValues(json, entityClass);
    }

    public void assertEquals(T expected, T actual) {
        Assert.assertEquals(new EntityWrapper<>(expected, entityComparator), new EntityWrapper<>(actual, entityComparator));
    }

    public void assertCollectionEquals(Collection<T> expected, Collection<T> actual) {
        Assert.assertEquals(map(expected), map(actual));
    }

    public List<EntityWrapper<T>> map(Collection<T> collection) {
        return collection.stream().map(e -> new EntityWrapper<>(e, entityComparator)).collect(Collectors.toList());
    }

    public ResultMatcher contentMatcher(T expect) {
        return content().string(
                new TestMatcher<T>(expect) {
                    @Override
                    protected boolean compare(T expected, String body) {
                        EntityWrapper<T> expectedForCompare = new EntityWrapper<>(expected, entityComparator);
                        EntityWrapper<T> actualForCompare = new EntityWrapper<>(fromJsonValue(body), entityComparator);
                        return expectedForCompare.equals(actualForCompare);
                    }
                });
    }

    public final ResultMatcher contentListMatcher(T... expected) {
        return contentListMatcher(Arrays.asList(expected));
    }

    public final ResultMatcher contentListMatcher(List<T> expected) {
        return content().string(new TestMatcher<List<T>>(expected) {
            @Override
            protected boolean compare(List<T> expected, String actual) {
                List<EntityWrapper<T>> expectedList = map(expected);
                List<EntityWrapper<T>> actualList = map(fromJsonValues(actual));
                return expectedList.equals(actualList);
            }
        });
    }

    public T fromJsonAction(ResultActions action) throws UnsupportedEncodingException {
        return fromJsonValue(TestUtil.getContent(action));
    }
}
