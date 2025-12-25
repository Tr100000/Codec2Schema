package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.impl.wrapped.CodecWithValuesHandler;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class DataComponentTypeCodecHandler implements CodecHandler<Codec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec == DataComponentType.PERSISTENT_CODEC;
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return CodecWithValuesHandler.createFromValues(BuiltInRegistries.DATA_COMPONENT_TYPE.stream()
                .filter(type -> !type.isTransient())
                .map(BuiltInRegistries.DATA_COMPONENT_TYPE::getKey)
                .map(Identifier::toString));
    }

    @Override
    public Optional<String> getName(Codec<?> codec) {
        return Optional.of("data_component_type_persistent");
    }
}
