package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.CompoundListCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.mixin.CompoundListCodecAccessor;

public class CompoundListCodecHandler implements CodecHandler<CompoundListCodec<?, ?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof CompoundListCodec<?, ?>;
    }

    @Override
    public JsonObject toSchema(CompoundListCodec<?, ?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        CompoundListCodecAccessor<?, ?> accessor = (CompoundListCodecAccessor<?, ?>)(Object)codec;
        assert accessor != null;
        json.add("propertyNames", context.requestDefinition(accessor.getKeyCodec()));
        JsonObject additionalProperties = context.requestDefinition(accessor.getElementCodec());
        json.add("additionalProperties", additionalProperties);
        return json;
    }
}
