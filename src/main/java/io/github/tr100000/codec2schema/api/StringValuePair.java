package io.github.tr100000.codec2schema.api;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public record StringValuePair<T>(T value, String str) {
    public <U> StringValuePair<U> mapValue(Function<? super T, ? extends U> mapper) {
        return new StringValuePair<>(mapper.apply(value), str);
    }

    public StringValuePair<T> mapStr(UnaryOperator<String> mapper) {
        return new StringValuePair<>(value, mapper.apply(str));
    }
}
