package io.github.tr100000.codec2schema.impl.specific;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.network.chat.Component;

public interface WrappedRestrictedComponentCodec extends Codec<Component> {
    Codec<Component> wrapped();
    int maxSize();

    @Override
    default <T> DataResult<Pair<Component, T>> decode(DynamicOps<T> ops, final T input) {
        return wrapped().decode(ops, input);
    }

    @Override
    default <T> DataResult<T> encode(final Component input, DynamicOps<T> ops, final T prefix) {
        return wrapped().encode(input, ops, prefix);
    }
}
