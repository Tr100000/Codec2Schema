package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public record NumberStreamCodecHandler(Codec<?> itemCodec) implements CodecHandler<PrimitiveCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof PrimitiveCodec<?>;
    }

    public static NumberStreamCodecHandler createHandler(PrimitiveCodec<?> codec) {
        if (codec == Codec.BYTE_BUFFER) {
            return new NumberStreamCodecHandler(Codec.BYTE);
        }
        else if (codec == Codec.INT_STREAM) {
            return new NumberStreamCodecHandler(Codec.INT);
        }
        else if (codec == Codec.LONG_STREAM) {
            return new NumberStreamCodecHandler(Codec.LONG);
        }
        else {
            return null;
        }
    }

    @Override
    public JsonObject toSchema(PrimitiveCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "array");
        json.add("items", context.requestDefinition(itemCodec));
        return json;
    }

    @Override
    public boolean shouldInline(PrimitiveCodec<?> codec) {
        return true;
    }
}
