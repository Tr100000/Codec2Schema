package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

public final class JsonUtils {
    private JsonUtils() {}

    public static JsonObject getOrCreateObject(JsonObject json, String name) {
        if (json.has(name)) {
            return json.getAsJsonObject(name);
        }
        else {
            JsonObject newJson = new JsonObject();
            json.add(name, newJson);
            return newJson;
        }
    }

    public static JsonArray getOrCreateArray(JsonObject json, String name) {
        if (json.has(name)) {
            return json.getAsJsonArray(name);
        }
        else {
            JsonArray newJson = new JsonArray();
            json.add(name, newJson);
            return newJson;
        }
    }

    @Contract("_, _, _ -> new")
    public static JsonObject schemaIfPropertyEquals(String propertyName, Object value, JsonObject thenSchema) {
        return schemaIfPropertyEquals(propertyName, value, thenSchema, null);
    }

    @Contract("_, _, _, _ -> new")
    public static JsonObject schemaIfPropertyEquals(String propertyName, Object value, JsonObject thenSchema, @Nullable JsonObject elseSchema) {
        JsonObject json = new JsonObject();

        JsonObject ifJson = new JsonObject();
        JsonObject properties = new JsonObject();
        JsonObject property = new JsonObject();
        setProperty(property, "const", value);
        properties.add(propertyName, property);
        ifJson.add("properties", properties);

        json.add("if", ifJson);
        json.add("then", thenSchema);
        if (elseSchema != null) json.add("else", elseSchema);
        return json;
    }

    @Contract(mutates = "param1")
    public static void setProperty(JsonObject json, String property, @Nullable Object value) {
        switch (value) {
            case null -> json.add(property, JsonNull.INSTANCE);
            case String s -> json.addProperty(property, s);
            case Number n -> json.addProperty(property, n);
            case Boolean b -> json.addProperty(property, b);
            case Character c -> json.addProperty(property, c);
            default -> throw new IllegalArgumentException(value.getClass().toString());
        }
    }

    @Contract(pure = true)
    public static String toSchemaSafeString(String str) {
        return str.replace(' ', '_').replace('/', '_');
    }
}
