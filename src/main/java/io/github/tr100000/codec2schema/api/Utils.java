package io.github.tr100000.codec2schema.api;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.mixin.RecursiveCodecAccessor;

import java.util.List;
import java.util.Optional;

public final class Utils {
    private Utils() {}

    public static <T> Optional<List<T>> getPossibleValues(Codec<T> codec) {
        return switch (codec) {
            case CodecWithValues<T> codecWithValues -> Optional.of(codecWithValues.possibleValues());
            case WrappedCodec<T> wrappedCodec -> getPossibleValues(wrappedCodec.original());
            case Codec.RecursiveCodec<T> recursiveCodec -> getPossibleValues(getRecursiveWrapped(recursiveCodec));
            default -> Optional.empty();
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Codec<T> getRecursiveWrapped(Codec.RecursiveCodec<T> codec) {
        return ((RecursiveCodecAccessor<T>)codec).getWrapped().get();
    }
}
