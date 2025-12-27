package io.github.tr100000.codec2schema.impl.wrapped;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.tr100000.codec2schema.api.NumberBound;

import java.util.Optional;

public interface WrappedRangedNumberCodec<A extends Number> extends Codec<A> {
    Codec<A> original();
    Optional<NumberBound<A>> min();
    Optional<NumberBound<A>> max();

    default boolean isInteger() {
        return true;
    }

    @Override
    default <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
        return original().decode(ops, input);
    }

    @Override
    default <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
        return original().encode(input, ops, prefix);
    }
}
