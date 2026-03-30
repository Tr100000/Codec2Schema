package io.github.tr100000.codec2schema.api;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public record ValueStringPair<T>(@Nullable T value, String str) {
    @Contract("_ -> new")
    public <U> ValueStringPair<U> mapValue(Function<? super @Nullable T, ? extends U> mapper) {
        return new ValueStringPair<>(mapper.apply(value), str);
    }

    @Contract("_ -> new")
    public ValueStringPair<T> mapStr(UnaryOperator<String> mapper) {
        return new ValueStringPair<>(value, mapper.apply(str));
    }

    @Contract(pure = true)
    public static <T, U> Function<ValueStringPair<T>, ValueStringPair<U>> mappingValue(Function<? super @Nullable T, ? extends U> mapper) {
        return pair -> pair.mapValue(mapper);
    }

    @Contract(pure = true)
    public static <T> UnaryOperator<ValueStringPair<T>> mappingStr(UnaryOperator<String> mapper) {
        return pair -> pair.mapStr(mapper);
    }

    @Contract(pure = true)
    public static <T> Predicate<ValueStringPair<T>> valueNotNull() {
        return pair -> pair.value != null;
    }
}
