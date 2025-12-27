package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.codec.WrappedMapCodec;

public class WrappedUnitMapCodecHandler implements MapCodecHandler<WrappedMapCodec<?>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof WrappedMapCodec<?> wrappedMapCodec && wrappedMapCodec.isUnitCodec();
    }

    @Override
    public JsonObject toSchema(WrappedMapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return new JsonObject();
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, WrappedMapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        // do nothing
    }
}
