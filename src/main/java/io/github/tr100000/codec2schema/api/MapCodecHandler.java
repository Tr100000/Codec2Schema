package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;

public interface MapCodecHandler<T extends MapCodec<?>> {
    default JsonObject toSchema(T codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        field(json, JsonUtils.getOrCreateObject(json, "properties"), JsonUtils.getOrCreateArray(json, "required"), codec, context, definitionContext);
        return json;
    }

    void field(JsonObject json, JsonObject properties, JsonArray required, T codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext);
}
