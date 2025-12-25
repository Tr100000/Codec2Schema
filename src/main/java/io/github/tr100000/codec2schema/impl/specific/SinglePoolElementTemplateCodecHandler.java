package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;

public class SinglePoolElementTemplateCodecHandler implements CodecHandler<Codec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec == SinglePoolElement.TEMPLATE_CODEC;
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return context.requestDefinition(Identifier.CODEC);
    }
}
