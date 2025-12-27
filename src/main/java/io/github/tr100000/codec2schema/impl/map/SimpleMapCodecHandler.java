package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.SimpleMapCodec;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

import java.util.stream.Stream;

public class SimpleMapCodecHandler<K, V> implements MapCodecHandler<SimpleMapCodec<K, V>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof SimpleMapCodec<?, ?>;
    }

    public static SimpleMapCodecHandler<?, ?> create() {
        return new SimpleMapCodecHandler<>();
    }

    @Override
    public JsonObject toSchema(SimpleMapCodec<K, V> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        json.add("propertyNames", createEnumFromKeys(codec.keys(JsonOps.INSTANCE)));
        return json;
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, SimpleMapCodec<K, V> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        throw new IllegalStateException();
    }

    private static JsonObject createEnumFromKeys(Stream<JsonElement> validKeys) {
        JsonObject json = new JsonObject();
        JsonArray enumArray = new JsonArray();
        validKeys.forEachOrdered(enumArray::add);
        json.add("enum", enumArray);
        return json;
    }
}
