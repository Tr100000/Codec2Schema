package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.PairMapCodec;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.mixin.PairMapCodecAccessor;

public class PairMapCodecHandler implements MapCodecHandler<PairMapCodec<?, ?>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof PairMapCodec<?, ?>;
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, PairMapCodec<?, ?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonArray allOf = JsonUtils.getOrCreateArray(json, "allOf");
        PairMapCodecAccessor<?, ?> accessor = (PairMapCodecAccessor<?, ?>)(Object)codec;
        allOf.add(context.requestDefinition(accessor.getFirst().codec()));
        allOf.add(context.requestDefinition(accessor.getSecond().codec()));
    }
}
