package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.EitherCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public class EitherCodecHandler implements CodecHandler<EitherCodec<?, ?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof EitherCodec<?, ?>;
    }

    @Override
    public JsonObject toSchema(EitherCodec<?, ?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        JsonArray anyOfArray = new JsonArray();

        anyOfArray.add(context.requestDefinition(codec.first()));
        anyOfArray.add(context.requestDefinition(codec.second()));

        json.add("anyOf", anyOfArray);
        return json;
    }

    @Override
    public boolean shouldInline(EitherCodec<?, ?> codec) {
        return true;
    }
}
