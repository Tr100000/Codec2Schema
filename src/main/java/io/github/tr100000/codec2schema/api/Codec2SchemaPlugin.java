package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface Codec2SchemaPlugin {
    default void earlyRegisterHandlers() {}
    default void registerHandlers() {}
    default void generateSchemas(SchemaExporter exporter) {}

    default boolean shouldRun() {
        return true;
    }

    default void writeExtraInfo(JsonObject pluginInfoJson) {}

    default @Nullable Identifier getId() {
        return null;
    }
}
