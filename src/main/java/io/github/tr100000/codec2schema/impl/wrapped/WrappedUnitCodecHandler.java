package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.WrappedUnitCodec;

public class WrappedUnitCodecHandler implements CodecHandler<WrappedUnitCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedUnitCodec<?>;
    }

    @Override
    public JsonObject toSchema(WrappedUnitCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return new JsonObject();
    }

    @Override
    public boolean shouldInline(WrappedUnitCodec<?> codec) {
        return true;
    }
}
