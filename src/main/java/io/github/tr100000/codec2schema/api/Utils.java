package io.github.tr100000.codec2schema.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.OptionalFieldCodec;
import io.github.tr100000.codec2schema.api.codec.WrappedCodec;
import io.github.tr100000.codec2schema.impl.map.WrappedDefaultedOptionalFieldMapCodec;
import io.github.tr100000.codec2schema.impl.map.WrappedFieldMapCodec;
import io.github.tr100000.codec2schema.mixin.OptionalFieldCodecAccessor;
import io.github.tr100000.codec2schema.mixin.RecursiveCodecAccessor;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class Utils {
    private Utils() {}

    private static final String RECURSIVE_MAP_CODEC_CLASS_NAME = "class com.mojang.serialization.MapCodec$RecursiveMapCodec";

    @Contract(pure = true)
    public static <T> Optional<List<ValueStringPair<T>>> getPossibleValues(Codec<T> codec) {
        for (CodecValueLister lister : CodecValueLister.LISTERS) {
            List<ValueStringPair<T>> values = lister.possibleValues(codec);
            if (values != null) return Optional.of(values);
        }

        return switch (codec) {
            case WrappedCodec<T> wrappedCodec -> getPossibleValues(wrappedCodec.original());
            case Codec.RecursiveCodec<T> recursiveCodec -> getPossibleValues(getRecursiveWrapped(recursiveCodec));
            default -> Optional.empty();
        };
    }

    @Contract(pure = true)
    public static <T> Optional<List<ValueStringPair<T>>> getPossibleValues(MapCodec<T> mapCodec) {
        if (mapCodec instanceof WrappedFieldMapCodec<T> wrappedFieldMapCodec) {
            return getPossibleValues(wrappedFieldMapCodec.original());
        }
        return Optional.empty();
    }

    @Contract(pure = true)
    public static String getFieldNameForDispatch(MapCodec<?> codec, Consumer<String> required) {
        return switch (codec) {
            case WrappedFieldMapCodec<?> wrappedFieldMapCodec -> {
                String fieldName = wrappedFieldMapCodec.fieldName();
                required.accept(fieldName);
                yield fieldName;
            }
            case OptionalFieldCodec<?> optionalFieldCodec -> ((OptionalFieldCodecAccessor<?>)optionalFieldCodec).getName();
            case WrappedDefaultedOptionalFieldMapCodec<?> wrappedOptionalFieldCodec -> wrappedOptionalFieldCodec.getName();
            default -> throw new IllegalArgumentException("Unexpected value: " + codec);
        };
    }

    @Contract(pure = true)
    public static boolean isRecursiveMapCodec(Object obj) {
        return obj.getClass().toString().equals(RECURSIVE_MAP_CODEC_CLASS_NAME);
    }

    @SuppressWarnings("unchecked")
    @Contract(pure = true)
    public static <T> Codec<T> getRecursiveWrapped(Codec.RecursiveCodec<T> codec) {
        return ((RecursiveCodecAccessor<T>)codec).getWrapped().get();
    }
}
