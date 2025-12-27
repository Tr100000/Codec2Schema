package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.network.chat.ComponentSerialization;

public class ComponentSerializationCodecHandler implements MapCodecHandler<MapCodec<?>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof ComponentSerialization.FuzzyCodec<?> || codec instanceof ComponentSerialization.StrictEither<?>;
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, MapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        if (codec instanceof ComponentSerialization.FuzzyCodec<?> fuzzyCodec) {
            fuzzyCodecSchema(json, fuzzyCodec, context);
        }
        else if (codec instanceof ComponentSerialization.StrictEither<?> strictEither) {
            strictEitherSchema(json, properties, strictEither, context);
        }
        else throw new IllegalArgumentException(); // this will never happen
    }

    private void fuzzyCodecSchema(JsonObject json, ComponentSerialization.FuzzyCodec<?> codec, SchemaContext context) {
        JsonArray anyOf = JsonUtils.getOrCreateArray(json, "anyOf");
        codec.codecs.forEach(c -> anyOf.add(context.requestDefinition(c.codec())));
    }

    private void strictEitherSchema(JsonObject json, JsonObject properties, ComponentSerialization.StrictEither<?> codec, SchemaContext context) {
        JsonObject ifJson = new JsonObject();
        properties.add(codec.typeFieldName, new JsonObject());
        ifJson.add("properties", properties);

        JsonArray allOf = JsonUtils.getOrCreateArray(json, "allOf");
        JsonObject entry = new JsonObject();
        entry.add("if", ifJson);
        entry.add("then", context.requestDefinition(codec.typed.codec()));
        entry.add("else", context.requestDefinition(codec.fuzzy.codec()));
        allOf.add(entry);
    }
}
