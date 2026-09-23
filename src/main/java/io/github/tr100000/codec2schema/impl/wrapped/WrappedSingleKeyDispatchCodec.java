package io.github.tr100000.codec2schema.impl.wrapped;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;

public record WrappedSingleKeyDispatchCodec<K, V>(Codec<V> wrapped, Codec<K> keyCodec, Function<V, K> keyGetter, Function<K, Codec<V>> valueCodecGetter) implements Codec<V> {
    @Override
    public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
        return wrapped.decode(ops, input);
    }

    @Override
    public <T> DataResult<T> encode(V input, DynamicOps<T> ops, T prefix) {
        return wrapped.encode(input, ops, prefix);
    }
}
