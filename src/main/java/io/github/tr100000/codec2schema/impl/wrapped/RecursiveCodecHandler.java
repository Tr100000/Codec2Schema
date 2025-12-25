package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.Utils;

public class RecursiveCodecHandler implements CodecHandler<Codec.RecursiveCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof Codec.RecursiveCodec<?>;
    }

    @Override
    public JsonObject toSchema(Codec.RecursiveCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return context.requestDefinition(Utils.getRecursiveWrapped(codec));
    }
}
