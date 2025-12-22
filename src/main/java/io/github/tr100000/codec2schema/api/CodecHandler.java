package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;

import java.util.Optional;

public interface CodecHandler<T extends Codec<?>> {
    JsonObject toSchema(T codec, SchemaContext context);

    default Optional<String> getName(T codec) {
        return Optional.empty();
    }

    default boolean shouldInline(T codec) {
        return false;
    }
}
