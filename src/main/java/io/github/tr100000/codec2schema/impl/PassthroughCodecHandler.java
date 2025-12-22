package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public class PassthroughCodecHandler implements CodecHandler<Codec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec == Codec.PASSTHROUGH;
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context) {
        return new JsonObject();
    }

    @Override
    public boolean shouldInline(Codec<?> codec) {
        return true;
    }
}
