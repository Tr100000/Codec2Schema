package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.resources.RegistryFileCodec;

public class RegistryFileCodecHandler implements CodecHandler<RegistryFileCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof RegistryFileCodec<?>;
    }

    @Override
    public JsonObject toSchema(RegistryFileCodec<?> codec, SchemaContext context) {
        return new JsonObject(); // TODO implement
    }
}
