package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.world.item.component.CustomData;

public class WrappedTypedEntityDataCodecHandler implements CodecHandler<WrappedTypedEntityDataCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedTypedEntityDataCodec<?>;
    }

    @Override
    public JsonObject toSchema(WrappedTypedEntityDataCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return context.requestDefinition(CustomData.COMPOUND_TAG_CODEC);
    }
}
