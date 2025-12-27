package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.impl.DispatchedMapCodecHandler;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class DataComponentValueMapCodecHandler implements CodecHandler<Codec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec == DataComponentType.VALUE_MAP_CODEC;
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return DispatchedMapCodecHandler.toSchema(
                BuiltInRegistries.DATA_COMPONENT_TYPE.stream()
                        .filter(type -> !type.isTransient())
                        .map(registered -> new ValueStringPair<>(registered, BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(registered).toString()))
                        .map(pair -> pair.mapValue(DataComponentType::codecOrThrow)),
                context
        );
    }
}
