package io.github.tr100000.codec2schema.impl.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import java.util.function.Function;
import java.util.stream.Stream;

public abstract class WrappedDispatchOptionalValueMapCodec<K, V> extends MapCodec<V> {
    public abstract MapCodec<V> original();
    public abstract String typeKey();
    public abstract String dispatchKey();
    public abstract Codec<K> keyCodec();
    public abstract Function<? super K, ? extends Codec<? extends V>> valueCodecFunction();

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return original().keys(ops);
    }

    @Override
    public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
        return original().decode(ops, input);
    }

    @Override
    public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        return original().encode(input, ops, prefix);
    }

    @Override
    public String toString() {
        return original().toString();
    }
}
