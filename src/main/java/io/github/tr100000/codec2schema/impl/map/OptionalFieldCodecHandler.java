package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.OptionalFieldCodec;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.mixin.OptionalFieldCodecAccessor;

public class OptionalFieldCodecHandler implements MapCodecHandler<OptionalFieldCodec<?>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof OptionalFieldCodec<?>;
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, OptionalFieldCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        OptionalFieldCodecAccessor<?> accessor = (OptionalFieldCodecAccessor<?>)codec;
        properties.add(accessor.getName(), context.requestDefinition(accessor.getElementCodec()));
    }
}
