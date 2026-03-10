package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public class WrappedConstrainedStringCodecHandler implements CodecHandler<WrappedConstrainedStringCodec> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedConstrainedStringCodec;
    }

    @Override
    public JsonObject toSchema(WrappedConstrainedStringCodec codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "string");
        if (codec.minLength() > 0) {
            json.addProperty("minLength", codec.minLength());
        }
        json.addProperty("maxLength", codec.maxLength());
        return json;
    }
}
