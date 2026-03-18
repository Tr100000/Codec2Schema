package io.github.tr100000.codec2schema.impl.wrapped;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.Nullable;

public interface WrappedStringRepresentableCodec<S extends StringRepresentable> extends CodecWithValuePairs<S> {
    @Override
    default @Nullable Codec<?> fallbackCodec() {
        return null;
    }
}
