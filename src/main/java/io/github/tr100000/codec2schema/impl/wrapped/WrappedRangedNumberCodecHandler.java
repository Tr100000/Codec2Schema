package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public class WrappedRangedNumberCodecHandler implements CodecHandler<WrappedRangedNumberCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedRangedNumberCodec<?>;
    }

    @Override
    public JsonObject toSchema(WrappedRangedNumberCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        json.addProperty("type", codec.isInteger() ? "integer" : "number");
        codec.min().ifPresent(bound -> json.addProperty(bound.exclusive() ? "exclusiveMinimum" : "minimum", bound.value()));
        codec.max().ifPresent(bound -> json.addProperty(bound.exclusive() ? "exclusiveMaximum" : "maximum", bound.value()));
        return json;
    }
}
