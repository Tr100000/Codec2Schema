package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

public interface CodecHandler<T extends Codec<?>> {
    JsonObject toSchema(T codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext);

    @Contract(pure = true)
    default Optional<String> getName(T codec) {
        return Optional.empty();
    }

    @Contract(pure = true)
    default boolean shouldInline(T codec) {
        return false;
    }
}
