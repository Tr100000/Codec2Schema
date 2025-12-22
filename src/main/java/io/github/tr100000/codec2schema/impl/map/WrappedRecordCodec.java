package io.github.tr100000.codec2schema.impl.map;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.stream.Stream;

public abstract class WrappedRecordCodec<A> extends MapCodec<A> {
    public abstract List<RecordCodecBuilder<?, ?>> fields();
    public abstract MapCodec<A> original();

    @Override
    public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        return original().decode(ops, input);
    }

    @Override
    public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        return original().encode(input, ops, prefix);
    }

    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return original().keys(ops);
    }

    @Override
    public String toString() {
        return original().toString();
    }
}
