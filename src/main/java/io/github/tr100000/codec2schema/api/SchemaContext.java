package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class SchemaContext {
    private final Map<String, DefinitionEntry> definitions = new Object2ObjectArrayMap<>();

    public boolean debugMode = false;
    public boolean allowInline = true;

    public void addDefinition(String name, Codec<?> codec, JsonObject json) {
        Objects.requireNonNull(name, "name is null");
        Objects.requireNonNull(json, "json is null");
        definitions.put(name, new FinishedDefinitionEntry(codec, json));
    }

    public JsonObject requestDefinition(String name, Supplier<JsonObject> definitionFactory) {
        if (!definitions.containsKey(name)) {
            addDefinition(name, null, definitionFactory.get());
        }
        return createRef(name);
    }

    public JsonObject requestDefinition(Codec<?> codec) {
        Objects.requireNonNull(codec, "codec is null");
        Optional<Map.Entry<String, DefinitionEntry>> entry = definitions.entrySet().stream()
                .filter(e -> codec.equals(e.getValue().codec()))
                .findAny();

        CodecHandler<Codec<?>> handler = CodecHandlerRegistry.getHandlerOrThrow(codec);

        if (allowInline && handler.shouldInline(codec)) {
            return handler.toSchema(codec, this);
        }

        if (entry.isEmpty()) {
            String name = getUniqueDefinition(handler.getName(codec).orElse("def"));
            definitions.put(name, new TentativeDefinitionEntry(codec));
            JsonObject json = handler.toSchema(codec, this);
            if (debugMode) {
                json.addProperty("_debug", codec.toString());
                json.addProperty("_handler", handler.getClass().toString());
            }
            addDefinition(name, codec, json);
            return createRef(name);
        }
        else {
            return createRef(entry.get().getKey());
        }
//        JsonObject json;
//        boolean inline = allowInline && handler.shouldInline(codec);
//        String name = "";
//
//        if (!inline && (entry.isEmpty() || entry.get() instanceof TentativeDefinitionEntry)) {
//            name = getUniqueDefinition("def");
//            definitions.put(name, new TentativeDefinitionEntry(codec));
//        }
//
//        if (entry.isPresent()) {
//            if (entry.get() instanceof FinishedDefinitionEntry finishedEntry) {
//                json = finishedEntry.json();
//            }
//            else {
//                json = createRef(name);
//            }
//        }
//        else {
//            json = handler.toSchema(codec, this);
//        }
//
//        if (inline) {
//            return json; // inline definition
//        }
//        else {
//            if (debugMode) {
//                json.addProperty("_debug", handler.getName(codec));
//                json.addProperty("_handler", handler.getClass().toString());
//            }
//            addDefinition(name, codec, json);
//            return createRef(name); // ref to definition
//        }
    }

    public JsonObject createRef(String name) {
        if (name.isBlank()) throw new IllegalArgumentException("Name must not be blank!");
        JsonObject json = new JsonObject();
        json.addProperty("$ref", "#/definitions/" + name);
        return json;
    }

    private String getUniqueDefinition(String name) {
        if (name.isBlank()) throw new IllegalArgumentException("Name must not be blank!");
        int n = 1;
        String finalName = name;
        while (definitions.containsKey(finalName)) {
            finalName = name + "_" + n++;
        }
        return finalName;
    }

    public void addDefinitions(JsonObject json) {
        JsonObject definitionsObject = new JsonObject();
        definitions.forEach((name, definition) -> {
            if (definition instanceof FinishedDefinitionEntry finishedEntry) {
                definitionsObject.add(name, finishedEntry.json());
            }
            else {
                throw new IllegalStateException(String.format("Definition %s was never completed!", name));
            }
        });
        json.add("definitions", definitionsObject);
    }

    interface DefinitionEntry {
        Codec<?> codec();
    }

    record TentativeDefinitionEntry(Codec<?> codec) implements DefinitionEntry {}
    record FinishedDefinitionEntry(@Nullable Codec<?> codec, JsonObject json) implements DefinitionEntry {}
}
