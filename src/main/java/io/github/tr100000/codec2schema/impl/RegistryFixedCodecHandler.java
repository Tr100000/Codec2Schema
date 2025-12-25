package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

public class RegistryFixedCodecHandler implements CodecHandler<RegistryFixedCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof RegistryFixedCodec<?>;
    }

    @Override
    public JsonObject toSchema(RegistryFixedCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return requestDefinition(codec.registryKey, context);
    }

    @NullMarked
    public static JsonObject requestDefinition(ResourceKey<? extends Registry<?>> key, SchemaContext context) {
        return BuiltInRegistries.REGISTRY.get(key.identifier())
                .map(Holder.Reference::value)
                .map(Registry::byNameCodec)
                .map(context::requestDefinition)
                .orElseGet(() -> context.requestDefinition(Identifier.CODEC));
    }

    @Override
    public Optional<String> getName(RegistryFixedCodec<?> codec) {
        return Optional.of("registry_fixed:" + codec.registryKey.identifier());
    }
}
