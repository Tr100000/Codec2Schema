package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.EitherMapCodec;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.mixin.EitherMapCodecAccessor;

public class EitherMapCodecHandler implements MapCodecHandler<EitherMapCodec<?, ?>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof EitherMapCodec<?, ?>;
    }

    @Override
    public JsonObject toSchema(EitherMapCodec<?, ?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        JsonArray anyOf = JsonUtils.getOrCreateArray(json, "anyOf");
        EitherMapCodecAccessor<?, ?> accessor = (EitherMapCodecAccessor<?, ?>)(Object)codec;
        anyOf.add(context.requestDefinition(accessor.getFirst().codec()));
        anyOf.add(context.requestDefinition(accessor.getSecond().codec()));
        return json;
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, EitherMapCodec<?, ?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonArray anyOf = new JsonArray();
        EitherMapCodecAccessor<?, ?> accessor = (EitherMapCodecAccessor<?, ?>)(Object)codec;
        anyOf.add(context.requestDefinition(accessor.getFirst().codec()));
        anyOf.add(context.requestDefinition(accessor.getSecond().codec()));

        JsonObject entry = new JsonObject();
        entry.add("anyOf", anyOf);
        JsonUtils.getOrCreateArray(json, "allOf").add(entry);
    }
}
