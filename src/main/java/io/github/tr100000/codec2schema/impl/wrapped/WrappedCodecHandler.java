package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.WrappedCodec;

public record WrappedCodecHandler(WrappedCodec<?> codec) implements CodecHandler<WrappedCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedCodec<?>;
    }

    @Override
    public JsonObject toSchema(WrappedCodec<?> codec, SchemaContext context) {
        return context.requestDefinition(codec.original());
    }

    @Override
    public boolean shouldInline(WrappedCodec<?> codec) {
        return true;
    }
}
