package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public record PrimitiveCodecHandler(String type) implements CodecHandler<PrimitiveCodec<?>> {
    public static boolean isPrimitiveCodec(Codec<?> codec) {
        return codec instanceof PrimitiveCodec<?>;
    }

    public static PrimitiveCodecHandler createHandler(PrimitiveCodec<?> codec) {
        if (codec == Codec.BOOL) {
            return new PrimitiveCodecHandler("boolean");
        }
        else if (codec == Codec.STRING) {
            return new PrimitiveCodecHandler("string");
        }
        else {
            return null;
        }
    }

    @Override
    public JsonObject toSchema(PrimitiveCodec<?> codec, SchemaContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        return json;
    }

    @Override
    public boolean shouldInline(PrimitiveCodec<?> codec) {
        return true;
    }
}
