package io.github.tr100000.codec2schema.impl.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public abstract class WrappedDefaultedOptionalFieldMapCodec<A> extends MapCodec<A> {
    public abstract MapCodec<A> original();
    public abstract String getName();
    public abstract Codec<A> getElementCodec();
    public abstract A defaultValue();
}
