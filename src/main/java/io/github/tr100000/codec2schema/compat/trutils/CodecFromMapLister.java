package io.github.tr100000.codec2schema.compat.trutils;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecValueLister;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.trutils.util.CodecFromMap;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CodecFromMapLister implements CodecValueLister {
    @Override
    public @Nullable <T> List<ValueStringPair<T>> possibleValues(Codec<T> codec) {
        if (codec instanceof CodecFromMap<?, T> codecFromMap) {
            return codecFromMap.map().values().stream()
                    .map(v -> new ValueStringPair<>(v, codecFromMap.map().inverse().get(v).toString()))
                    .toList();
        }
        else return null;
    }
}
