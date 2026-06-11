package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.Codec2Schema;
import io.github.tr100000.codec2schema.Codec2SchemaConfig;
import io.github.tr100000.codec2schema.ExportContext;
import io.github.tr100000.codec2schema.SingularReferenceInliner;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.SharedConstants;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SchemaExporter implements BiConsumer<Codec<?>, String> {
    private @Nullable ExportContext exportContext;
    private @Nullable Consumer<SchemaContext> options;

    public void accept(Codec<?> codec, String path) {
        export(codec, Codec2Schema.EXPORT_ROOT_DIR.resolve(path), path);
    }

    public void accept(Codec<?> codec, String... path) {
        Path p = Codec2Schema.EXPORT_ROOT_DIR;
        for (String str : path) {
            p = p.resolve(str);
        }
        export(codec, p, path);
    }

    public void setExportContext(ExportContext exportContext) {
        this.exportContext = exportContext;
    }

    public void setOption(Consumer<SchemaContext> options) {
        Objects.requireNonNull(options);
        if (this.options == null) {
            this.options = options;
        }
        else {
            this.options = this.options.andThen(options);
        }
    }

    public void clearOptions() {
        options = null;
    }

    private JsonObject convert(Codec<?> codec) {
        SchemaContext context = new SchemaContext();
        if (options != null) options.accept(context);
        JsonObject json = new JsonObject();
        json.addProperty("$schema", "https://json-schema.org/draft-07/schema");
        JsonUtils.addAllPropertiesFrom(json, context.requestDefinition(codec));
        if (Codec2SchemaConfig.INSTANCE.addExportedWith()) {
            JsonObject exportedWith = new JsonObject();
            exportedWith.addProperty("minecraft", SharedConstants.getCurrentVersion().name());
            exportedWith.addProperty("codec2schema", Codec2Schema.version());
            if (exportContext != null) {
                JsonObject exportedBy = JsonUtils.getOrCreateObject(exportedWith, "exportedBy");
                exportedBy.addProperty("pluginId", exportContext.getPluginId().toString());
                ModMetadata exportedByMetadata = exportContext.mod().getMetadata();
                exportedBy.addProperty("modVersion", exportedByMetadata.getVersion().getFriendlyString());
            }
            json.add("exportedWith", exportedWith);
        }
        context.addDefinitions(json);
        return json;
    }

    private void export(Codec<?> codec, Path path, String... strPath) {
        try {
            Files.createDirectories(path.getParent());
            Files.deleteIfExists(path);

            long startTimeMillis = System.currentTimeMillis();
            JsonObject json = convert(codec);
            if (Codec2SchemaConfig.INSTANCE.inlineSingularReference()) {
                json = SingularReferenceInliner.process(json);
            }
            Files.writeString(path, Codec2Schema.GSON.toJson(json));
            long endTimeMillis = System.currentTimeMillis();

            if (Codec2Schema.LOGGER.isInfoEnabled()) {
                Codec2Schema.LOGGER.info("Exported codec to {} ({}ms)", String.join("/", strPath), endTimeMillis - startTimeMillis);
            }
        }
        catch (IOException e) {
            Codec2Schema.LOGGER.error("Failed to write codec schema to {}", String.join("/", strPath), e);
        }
    }
}
