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
    public JsonObject toSchema(UnboundedMapCodec<?, ?> codec, SchemaContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        JsonObject propertyNames = new JsonObject();
        propertyNames.addProperty("type", "string");
        json.add("propertyNames", propertyNames);
        JsonObject additionalProperties = context.requestDefinition(codec.elementCodec());
        json.add("additionalProperties", additionalProperties);
        return json;
    }
}
