package io.github.tr100000.codec2schema.api;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface CodecValueLister {
    List<CodecValueLister> LISTERS = new ObjectArrayList<>();

    @Contract(pure = true)
    default <T> @Nullable List<ValueStringPair<T>> possibleValues(Codec<T> codec) {
        return null;
    }
}
