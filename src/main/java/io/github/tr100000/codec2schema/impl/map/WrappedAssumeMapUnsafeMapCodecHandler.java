package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public class WrappedAssumeMapUnsafeMapCodecHandler implements MapCodecHandler<WrappedAssumeMapUnsafeMapCodec<?>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof WrappedAssumeMapUnsafeMapCodec<?>;
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, WrappedAssumeMapUnsafeMapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonUtils.getOrCreateArray(json, "allOf").add(context.requestDefinition(codec.originalBeforeAssume()));
    }
}
