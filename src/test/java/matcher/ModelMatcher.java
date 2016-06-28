package matcher;

import org.junit.Assert;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Sheremet on 28.06.2016.
 */
public class ModelMatcher<T,R> {
    protected Function<T, R> entityConverter;

    public ModelMatcher(Function<T, R> entityConverter) {
        this.entityConverter = entityConverter;
    }

    public void assertEquals(T expected, T actual) {
        Assert.assertEquals(entityConverter.apply(expected), entityConverter.apply(actual));
    }

    public void assertCollectionEquals(Collection<T> expected, Collection<T> actual) {
        Assert.assertEquals(map(expected, entityConverter), map(actual, entityConverter));
    }

    public static <S, T> Collection<T> map(Collection<S> collection, Function<S, T> converter) {
        return collection.stream().map(converter).collect(Collectors.toList());
    }
}
