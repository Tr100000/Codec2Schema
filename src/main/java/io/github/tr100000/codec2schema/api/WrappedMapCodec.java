package io.github.tr100000.codec2schema.api;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import java.util.stream.Stream;

public abstract class WrappedMapCodec<A> extends MapCodec<A> {
    public abstract MapCodec<?> original();

    @SuppressWarnings("unchecked")
    public MapCodec<A> getWrapped() {
        return (MapCodec<A>)original();
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return getWrapped().keys(ops);
    }

    @Override
    public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
        return getWrapped().decode(ops, input);
    }

    @Override
    public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        return getWrapped().encode(input, ops, prefix);
    }

    @Override
    public String toString() {
        return getWrapped().toString();
    }

    public boolean isUnitCodec() {
        return false;
    }
}
