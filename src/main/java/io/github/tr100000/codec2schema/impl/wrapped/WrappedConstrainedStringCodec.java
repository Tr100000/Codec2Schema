package io.github.tr100000.codec2schema.impl.wrapped;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public record WrappedConstrainedStringCodec(Codec<String> original, int minLength, int maxLength) implements Codec<String> {
    @Override
    public <T> DataResult<Pair<String, T>> decode(DynamicOps<T> ops, T input) {
        return original().decode(ops, input);
    }

    @Override
    public <T> DataResult<T> encode(String input, DynamicOps<T> ops, T prefix) {
        return original().encode(input, ops, prefix);
    }
}
