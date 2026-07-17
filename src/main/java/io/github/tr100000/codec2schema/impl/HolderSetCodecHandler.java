package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.mixin.HolderSetCodecAccessor;
import net.minecraft.resources.HolderSetCodec;

public record HolderSetCodecHandler(HolderSetCodec<?> codec) implements CodecHandler<HolderSetCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof HolderSetCodec<?>;
    }

    @Override
    public JsonObject toSchema(HolderSetCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return context.requestDefinition(((HolderSetCodecAccessor<?>)codec).getTagKeyOrValuesCodec());
    }
}
