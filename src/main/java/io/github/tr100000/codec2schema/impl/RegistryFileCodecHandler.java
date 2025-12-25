package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.resources.RegistryFileCodec;

import java.util.Optional;

public class RegistryFileCodecHandler implements CodecHandler<RegistryFileCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof RegistryFileCodec<?>;
    }

    @Override
    public JsonObject toSchema(RegistryFileCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {

        if (codec.allowInline) {
            JsonObject json = new JsonObject();
            JsonObject strObj = RegistryFixedCodecHandler.requestDefinition(codec.registryKey, context);
            JsonObject inlineObj = context.requestDefinition(codec.elementCodec);
            JsonArray anyOf = new JsonArray();
            anyOf.add(strObj);
            anyOf.add(inlineObj);
            json.add("anyOf", anyOf);
            return json;
        }
        else {
            return RegistryFixedCodecHandler.requestDefinition(codec.registryKey, context);
        }
    }

    @Override
    public Optional<String> getName(RegistryFileCodec<?> codec) {
        if (codec.allowInline) {
            return Optional.of("registry_file_inline:" + codec.registryKey.identifier());
        }
        else {
            return Optional.of("registry_file:" + codec.registryKey.identifier());
        }
    }
}
