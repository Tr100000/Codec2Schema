package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record NumberCodecHandler(String type, Optional<Number> min, Optional<Number> max) implements CodecHandler<Codec<?>> {
    public NumberCodecHandler(String type, Number min, Number max) {
        this(type, Optional.of(min), Optional.of(max));
    }

    public static boolean predicate(Codec<?> codec) {
        return codec instanceof PrimitiveCodec<?>;
    }

    public static NumberCodecHandler createHandler(Codec<?> codec) {
        if (codec == Codec.BYTE) {
            return new NumberCodecHandler("integer", Byte.MIN_VALUE, Byte.MAX_VALUE);
        } else if (codec == Codec.SHORT) {
            return new NumberCodecHandler("integer", Short.MIN_VALUE, Short.MAX_VALUE);
        } else if (codec == Codec.INT) {
            return new NumberCodecHandler("integer", Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else if (codec == Codec.LONG) {
            return new NumberCodecHandler("integer", Long.MIN_VALUE, Long.MAX_VALUE);
        } else if (codec == Codec.FLOAT || codec == Codec.DOUBLE) {
            return new NumberCodecHandler("number", Optional.empty(), Optional.empty());
        } else {
            return null;
        }
    }

    public static boolean rangePredicate(Codec<?> codec) {
        return codec == ExtraCodecs.UNSIGNED_BYTE;
    }

    public static NumberCodecHandler createRangedHandler(Codec<?> codec) {
        if (codec == ExtraCodecs.UNSIGNED_BYTE) {
            return new NumberCodecHandler("integer", Optional.empty(), Optional.of(255L));
        } else {
            return null;
        }
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        min.ifPresent(m -> json.addProperty("minimum", m));
        max.ifPresent(m -> json.addProperty("maximum", m));
        return json;
    }

    @Override
    public boolean shouldInline(Codec<?> codec) {
        return true;
    }
}
