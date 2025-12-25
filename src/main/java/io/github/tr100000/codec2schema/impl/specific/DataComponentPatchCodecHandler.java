package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.Codec2Schema;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class DataComponentPatchCodecHandler implements CodecHandler<Codec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec == DataComponentPatch.CODEC;
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        JsonObject properties = new JsonObject();

        for (DataComponentType<?> type : BuiltInRegistries.DATA_COMPONENT_TYPE) {
            if (type.isTransient()) continue;

            Identifier id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
            if (context.debugMode) Codec2Schema.LOGGER.info(id.toString());
            properties.add(id.toString(), context.requestDefinition(type.codecOrThrow()));
            properties.add("!" + id, new JsonObject());
        }

        json.add("properties", properties);
        return json;
    }
}
