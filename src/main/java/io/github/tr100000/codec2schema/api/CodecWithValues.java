package io.github.tr100000.codec2schema.api;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.List;
import java.util.Optional;

public interface CodecWithValues<A> extends Codec<A> {
    List<StringValuePair<A>> possibleValues();
    Codec<A> original();

    @Override
    default <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
        return original().decode(ops, input);
    }

    @Override
    default <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
        return original().encode(input, ops, prefix);
    }

    default Optional<String> getName() {
        return Optional.empty();
    }
}
