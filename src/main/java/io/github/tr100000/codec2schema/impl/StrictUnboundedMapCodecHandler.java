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
        json.addProperty("type", "object");
        json.add("propertyNames", context.requestDefinition(codec.keyCodec()));
        json.add("additionalProperties", context.requestDefinition(codec.elementCodec()));
        return json;
    }
}
