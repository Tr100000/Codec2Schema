package io.github.tr100000.codec2schema.impl.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public abstract class WrappedFieldMapCodec<A> extends MapCodec<A> {
    public abstract Codec<A> original();
    public abstract String fieldName();
}
