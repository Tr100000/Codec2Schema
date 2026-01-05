package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public record UnboundedMapCodecHandler(UnboundedMapCodec<?, ?> codec) implements CodecHandler<UnboundedMapCodec<?, ?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof UnboundedMapCodec<?, ?>;
    }

    @Override
    public JsonObject toSchema(UnboundedMapCodec<?, ?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return toSchema(codec.keyCodec(), codec.elementCodec(), context);
    }

    public static JsonObject toSchema(Codec<?> keyCodec, Codec<?> elementCodec, SchemaContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        json.add("propertyNames", context.requestDefinition(keyCodec));
        JsonObject additionalProperties = context.requestDefinition(elementCodec);
        json.add("additionalProperties", additionalProperties);
        json.addProperty("_info", "UNBOUNDED MAP CODEC");
        return json;
    }
}
