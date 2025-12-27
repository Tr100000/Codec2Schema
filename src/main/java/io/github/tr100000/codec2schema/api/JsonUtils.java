package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

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

    public static JsonObject schemaIfPropertyEquals(String propertyName, Object value, JsonObject thenSchema) {
        return schemaIfPropertyEquals(propertyName, value, thenSchema, null);
    }

    public static JsonObject schemaIfPropertyEquals(String propertyName, Object value, JsonObject thenSchema, JsonObject elseSchema) {
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

    public static void setProperty(JsonObject json, String property, Object value) {
        switch (value) {
            case null -> json.add(property, JsonNull.INSTANCE);
            case String s -> json.addProperty(property, s);
            case Number n -> json.addProperty(property, n);
            case Boolean b -> json.addProperty(property, b);
            case Character c -> json.addProperty(property, c);
            default -> throw new IllegalArgumentException(value.getClass().toString());
        }
    }

    public static String toSchemaSafeString(String str) {
        return str.replaceAll(" ", "_").replaceAll("/", "_");
    }
}
