package io.github.tr100000.codec2schema.impl.wrapped;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.StringRepresentable;

public interface WrappedStringRepresentableCodec<S extends StringRepresentable> extends Codec<S> {
    Codec<S> getOriginal();
    String[] getEnumValues();

    @Override
    default <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
        return getOriginal().decode(ops, input);
    }

    @Override
    default <T> DataResult<T> encode(S stringRepresentable, DynamicOps<T> dynamicOps, T object) {
        return getOriginal().encode(stringRepresentable, dynamicOps, object);
    }
}
