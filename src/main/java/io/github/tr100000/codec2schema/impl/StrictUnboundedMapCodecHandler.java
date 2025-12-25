package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.util.ExtraCodecs;

public class StrictUnboundedMapCodecHandler implements CodecHandler<ExtraCodecs.StrictUnboundedMapCodec<?, ?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof ExtraCodecs.StrictUnboundedMapCodec<?, ?>;
    }

    @Override
    public JsonObject toSchema(ExtraCodecs.StrictUnboundedMapCodec<?, ?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();

        JsonObject propertyNames = context.requestDefinition(codec.keyCodec());
        json.add("propertyNames", propertyNames);

        JsonObject additionalProperties = context.requestDefinition(codec.elementCodec());
        json.add("additionalProperties", additionalProperties);

        return json;
    }
}
