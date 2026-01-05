package io.github.tr100000.codec2schema.compat.fabric;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.impl.UnboundedMapCodecHandler;
import net.fabricmc.fabric.impl.dimension.FailSoftMapCodec;

public class FailSoftMapCodecHandler implements CodecHandler<FailSoftMapCodec<?, ?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof FailSoftMapCodec<?, ?>;
    }

    @Override
    public JsonObject toSchema(FailSoftMapCodec<?, ?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return UnboundedMapCodecHandler.toSchema(codec.keyCodec(), codec.elementCodec(), context);
    }
}
