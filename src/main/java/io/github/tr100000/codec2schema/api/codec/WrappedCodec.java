package io.github.tr100000.codec2schema.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public interface WrappedCodec<A> extends Codec<A> {
    Codec<A> original();

    default Codec<A> getWrapped() {
        return original();
    }

    @Override
    default <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
        return getWrapped().decode(ops, input);
    }

    @Override
    default <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
        return getWrapped().encode(input, ops, prefix);
    }
}
