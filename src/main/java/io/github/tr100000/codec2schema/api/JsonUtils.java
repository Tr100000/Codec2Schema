package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonArray;
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

    public static JsonObject schemaIfPropertyEquals(String propertyName, String value, JsonObject schema) {
        JsonObject json = new JsonObject();

        JsonObject ifJson = new JsonObject();
        JsonObject properties = new JsonObject();
        JsonObject property = new JsonObject();
        property.addProperty("const", value);
        properties.add(propertyName, property);
        ifJson.add("properties", properties);

        json.add("if", ifJson);
        json.add("then", schema);
        return json;
    }

    public static String toSchemaSafeString(String str) {
        return str.replaceAll(" ", "_").replaceAll("/", "_");
    }
}
