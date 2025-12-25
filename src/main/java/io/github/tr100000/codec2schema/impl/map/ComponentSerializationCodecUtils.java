package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.network.chat.ComponentSerialization;

public final class ComponentSerializationCodecUtils {
    private ComponentSerializationCodecUtils() {}

    public static boolean canHandle(MapCodec<?> codec) {
        return codec instanceof ComponentSerialization.FuzzyCodec<?> || codec instanceof ComponentSerialization.StrictEither<?>;
    }

    public static JsonObject toSchema(MapCodec<?> codec, SchemaContext context) {
        if (codec instanceof ComponentSerialization.FuzzyCodec<?> fuzzyCodec) {
            return fuzzyCodecSchema(new JsonObject(), fuzzyCodec, context);
        }
        else if (codec instanceof ComponentSerialization.StrictEither<?> strictEither) {
            return strictEitherSchema(new JsonObject(), strictEither, context);
        }
        else throw new IllegalArgumentException(); // this will never happen
    }

    public static JsonObject fuzzyCodecSchema(JsonObject json, ComponentSerialization.FuzzyCodec<?> codec, SchemaContext context) {
        JsonArray anyOf = JsonUtils.getOrCreateArray(json, "anyOf");
        codec.codecs.forEach(c -> anyOf.add(context.requestDefinition(c.codec())));
        return json;
    }

    public static JsonObject strictEitherSchema(JsonObject json, ComponentSerialization.StrictEither<?> codec, SchemaContext context) {
        JsonObject ifJson = new JsonObject();
        JsonObject properties = new JsonObject();
        properties.add(codec.typeFieldName, new JsonObject());
        ifJson.add("properties", properties);

        JsonArray allOf = JsonUtils.getOrCreateArray(json, "allOf");
        JsonObject entry = new JsonObject();
        entry.add("if", ifJson);
        entry.add("then", context.requestDefinition(codec.typed.codec()));
        entry.add("else", context.requestDefinition(codec.fuzzy.codec()));
        allOf.add(entry);
        return json;
    }
}
