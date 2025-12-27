package io.github.tr100000.codec2schema.compat.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.mixin.compat.fabric.CustomUnbakedBlockStateModelRegistryAccessor;

// This is only used once
public class KeyExistsCodecHandler implements MapCodecHandler<MapCodec<?>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec == CustomUnbakedBlockStateModelRegistryAccessor.getVariantMapCodec();
    }

    @Override
    public JsonObject toSchema(MapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();

        JsonObject ifJson = new JsonObject();
        JsonObject properties = new JsonObject();
        properties.add(CustomUnbakedBlockStateModelRegistryAccessor.getTypeKey(), new JsonObject());
        ifJson.add("properties", properties);

        json.add("if", ifJson);
        json.add("then", context.requestDefinition(CustomUnbakedBlockStateModelRegistryAccessor.getCustomModelMapCodec().codec()));
        json.add("else", context.requestDefinition(CustomUnbakedBlockStateModelRegistryAccessor.getSimpleModelMapCodec().codec()));

        return json;
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, MapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonUtils.getOrCreateArray(json, "allOf").add(toSchema(codec, context, definitionContext));
    }
}
