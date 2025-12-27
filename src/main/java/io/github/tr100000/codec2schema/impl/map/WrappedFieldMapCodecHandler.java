package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public class WrappedFieldMapCodecHandler implements MapCodecHandler<WrappedFieldMapCodec<?>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof WrappedFieldMapCodec<?>;
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, WrappedFieldMapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        properties.add(codec.fieldName(), context.requestDefinition(codec.original()));
        required.add(codec.fieldName());
    }
}
