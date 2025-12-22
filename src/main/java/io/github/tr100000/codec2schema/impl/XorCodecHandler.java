package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.EitherCodec;
import com.mojang.serialization.codecs.XorCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public class XorCodecHandler implements CodecHandler<XorCodec<?, ?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof EitherCodec<?, ?>;
    }
    
    @Override
    public JsonObject toSchema(XorCodec<?, ?> codec, SchemaContext context) {
        JsonObject json = new JsonObject();
        JsonArray anyOfArray = new JsonArray();

        anyOfArray.add(context.requestDefinition(codec.first()));
        anyOfArray.add(context.requestDefinition(codec.second()));

        json.add("oneOf", anyOfArray);
        return json;
    }

    @Override
    public boolean shouldInline(XorCodec<?, ?> codec) {
        return true;
    }
}
