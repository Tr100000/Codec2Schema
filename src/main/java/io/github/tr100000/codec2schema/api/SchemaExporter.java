package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.Codec2Schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SchemaExporter implements BiConsumer<Codec<?>, String> {
    private Consumer<SchemaContext> options = ignored -> {};

    public void accept(Codec<?> codec, String path) {
        export(codec, Codec2Schema.EXPORT_ROOT_DIR.resolve(path));
    }

    public void accept(Codec<?> codec, String... path) {
        Path p = Codec2Schema.EXPORT_ROOT_DIR;
        for (String str : path) {
            p = p.resolve(str);
        }
        export(codec, p);
    }

    public void setOptions(Consumer<SchemaContext> options) {
        this.options = options;
    }

    private JsonObject convert(Codec<?> codec) {
        SchemaContext context = new SchemaContext();
        options.accept(context);
        JsonObject json = context.requestDefinition(codec);
        json.addProperty("$schema", "https://json-schema.org/draft-07/schema");
        context.addDefinitions(json);
        return json;
    }

    private void export(Codec<?> codec, Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.deleteIfExists(path);

            long startTimeMillis = System.currentTimeMillis();
            JsonObject json = convert(codec);
            Files.writeString(path, Codec2Schema.GSON.toJson(json));
            long endTimeMillis = System.currentTimeMillis();

            Codec2Schema.LOGGER.info("Exported codec to {} ({}ms)", path, endTimeMillis - startTimeMillis);
        }
        catch (IOException e) {
            Codec2Schema.LOGGER.error("Failed to write codec schema to {}", path, e);
        }
    }
}
