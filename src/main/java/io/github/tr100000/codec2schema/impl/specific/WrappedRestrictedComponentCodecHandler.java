package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.network.chat.ComponentSerialization;

public class WrappedRestrictedComponentCodecHandler implements CodecHandler<WrappedRestrictedComponentCodec> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedRestrictedComponentCodec;
    }

    @Override
    public JsonObject toSchema(WrappedRestrictedComponentCodec codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return context.requestDefinition(ComponentSerialization.CODEC);
    }
}
