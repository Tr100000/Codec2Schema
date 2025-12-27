package io.github.tr100000.codec2schema.impl.wrapped;

import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public abstract class WrappedEnumCodec<E extends Enum<E> & StringRepresentable> extends StringRepresentable.EnumCodec<E> implements CodecWithValuePairs<E> {
    protected WrappedEnumCodec(E[] enums, Function<String, E> function) {
        super(enums, function);
    }
}
