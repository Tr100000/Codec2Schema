package io.github.tr100000.codec2schema.impl.wrapped;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public abstract class WrappedEnumCodec<E extends Enum<E> & StringRepresentable> extends StringRepresentable.EnumCodec<E> {
    protected WrappedEnumCodec(E[] enums, Function<String, E> function) {
        super(enums, function);
    }

    public abstract Codec<E> getOriginal();
    public abstract String[] getEnumValues();

    @Override
    public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
        return getOriginal().decode(ops, input);
    }

    @Override
    public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
        return getOriginal().encode(input, ops, prefix);
    }
}
