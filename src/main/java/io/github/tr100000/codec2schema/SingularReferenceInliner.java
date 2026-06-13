package io.github.tr100000.codec2schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import java.util.Map;

// Increases output size due to indentation, still has many issues
@ApiStatus.Internal
public final class SingularReferenceInliner {
    private SingularReferenceInliner() {}

    public static JsonObject process(JsonObject original) {
        try {
            Map<String, Data> referenceMap = new Object2ObjectOpenHashMap<>();
            JsonObject json = original.deepCopy();

            String root = json.get("$ref").getAsString();
            JsonObject definitions = json.getAsJsonObject("definitions");

            definitions.keySet().forEach(d -> referenceMap.put(d, new Data()));
            referenceMap.get(getActualRef(root)).invalidate();
            definitions.asMap().forEach((key, value) -> count(key, JsonReplaceable.of(definitions, key), (JsonObject)value, referenceMap));

            referenceMap.forEach((name, data) -> {
                if (data.count == 1 && data.lastReplaceable != null) {
                    JsonElement def = definitions.remove(name);
                    data.lastReplaceable.replaceWith(def);
                    Codec2Schema.LOGGER.debug("Inlined {}", name);
                }
            });

            return json;
        }
        catch (Exception e) {
            Codec2Schema.LOGGER.warn("Failed to process original", e);
            return original;
        }
    }

    private static void count(String rootDef, JsonReplaceable key, JsonObject json, Map<String, Data> referenceMap) {
        JsonElement ref = json.get("$ref");
        if (ref != null && ref.isJsonPrimitive()) {
            String actaulRef = getActualRef(ref.getAsString());
            if (rootDef.equals(actaulRef)) { // recursive!
                referenceMap.get(rootDef).invalidate();
                return;
            }
            referenceMap.get(actaulRef).incrementWith(key);
        }
        else {
            json.asMap().forEach((k, v) -> {
                if (v instanceof JsonObject jObj) {
                    count(rootDef, JsonReplaceable.of(jObj, k), jObj, referenceMap);
                }
                else if (v instanceof JsonArray jArr) {
                    for (int i = 0; i < jArr.size(); i++) {
                        JsonElement j = jArr.get(i);
                        if (j instanceof JsonObject jObj) {
                            count(rootDef, JsonReplaceable.ofArray(jArr, i), jObj, referenceMap);
                        }
                    }
                }
            });
        }
    }

    private static String getActualRef(String ref) {
        return ref.substring("#/definitions/".length());
    }

    static final class Data {
        int count = 0;
        @Nullable JsonReplaceable lastReplaceable;

        void incrementWith(JsonReplaceable key) {
            if (count >= 0) count++;
            lastReplaceable = count <= 1 ? key : null;
        }

        void invalidate() {
            count = -1;
            lastReplaceable = null;
        }
    }

    @FunctionalInterface
    interface JsonReplaceable {
        void replaceWith(JsonElement value);

        static JsonReplaceable of(JsonObject json, String property) {
            return newValue -> json.add(property, newValue);
        }

        static JsonReplaceable ofArray(JsonArray array, int index) {
            return newValue -> array.set(index, newValue);
        }
    }
}
