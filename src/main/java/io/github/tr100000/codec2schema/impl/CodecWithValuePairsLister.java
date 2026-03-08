package io.github.tr100000.codec2schema.impl;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecValueLister;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CodecWithValuePairsLister implements CodecValueLister {
    @Override
    public @Nullable <T> List<ValueStringPair<T>> possibleValues(Codec<T> codec) {
        if (codec instanceof CodecWithValuePairs<T> codecWithValuePairs) {
            return codecWithValuePairs.possibleValues();
        }
        else return null;
    }
}
