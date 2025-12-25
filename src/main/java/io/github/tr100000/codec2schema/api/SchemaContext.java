package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.Codec2Schema;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.jspecify.annotations.Nullable;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class SchemaContext {
    private final Map<String, DefinitionEntry> definitions = new Object2ObjectArrayMap<>();
    private final LinkedList<String> definitionStack = new LinkedList<>();

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
            return handler.toSchema(codec, this, new DefinitionContext(Optional.empty(), definitionStack));
        }

        if (entry.isEmpty()) {
            String name = getUniqueDefinition(JsonUtils.toSchemaSafeString(handler.getName(codec).orElse("def")));
            definitions.put(name, new TentativeDefinitionEntry(codec));
            if (debugMode) Codec2Schema.LOGGER.info(codec.toString());
            definitionStack.push(name);
            JsonObject json = handler.toSchema(codec, this, new DefinitionContext(Optional.of(name), definitionStack));
            definitionStack.pop();
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
    }

    public Optional<String> tryGetDefinitionName(Codec<?> codec) {
        return definitions.entrySet().stream()
                .filter(e -> codec.equals(e.getValue().codec()))
                .map(Map.Entry::getKey)
                .findAny();
    }

    public JsonObject createRef(String name) {
        if (name.isBlank()) throw new IllegalArgumentException("Name must not be blank!");
        JsonObject json = new JsonObject();
        json.addProperty("$ref", "#/definitions/" + name);
        return json;
    }

    public void cancel(String name) {
        if (definitions.get(name) instanceof TentativeDefinitionEntry) {
            definitions.remove(name);
        }
        else {
            throw new IllegalStateException("Definition " + name + " cannot be cancelled");
        }
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

    public record DefinitionContext(Optional<String> name, LinkedList<String> definitionStack) {}
}
