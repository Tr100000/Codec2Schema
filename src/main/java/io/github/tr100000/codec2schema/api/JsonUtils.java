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
}
