package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.resources.RegistryFixedCodec;

public class RegistryFixedCodecHandler implements CodecHandler<RegistryFixedCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof RegistryFixedCodec<?>;
    }

    @Override
    public JsonObject toSchema(RegistryFixedCodec<?> codec, SchemaContext context) {
        // TODO do this properly
        JsonObject json = new JsonObject();
        json.addProperty("_registry", codec.registryKey.identifier().toString());
        json.addProperty("type", "string");
        return json;
    }
}
