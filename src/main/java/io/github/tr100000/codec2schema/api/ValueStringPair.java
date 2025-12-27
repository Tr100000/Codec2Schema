package io.github.tr100000.codec2schema.api;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public record ValueStringPair<T>(T value, String str) {
    public <U> ValueStringPair<U> mapValue(Function<? super T, ? extends U> mapper) {
        return new ValueStringPair<>(mapper.apply(value), str);
    }

    public ValueStringPair<T> mapStr(UnaryOperator<String> mapper) {
        return new ValueStringPair<>(value, mapper.apply(str));
    }

    public static <T> Predicate<ValueStringPair<T>> valueNotNull() {
        return pair -> pair.value != null;
    }
}
