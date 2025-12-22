package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public class WrappedRestrictedComponentCodecHandler implements CodecHandler<WrappedRestrictedComponentCodec> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedRestrictedComponentCodec;
    }

    @Override
    public JsonObject toSchema(WrappedRestrictedComponentCodec codec, SchemaContext context) {
        return new JsonObject(); // TODO implement
    }
}
