package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.network.chat.ComponentSerialization;

public class ComponentCodecHandler implements CodecHandler<Codec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec == ComponentSerialization.CODEC;
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context) {
        return new JsonObject(); // TODO
    }
}
