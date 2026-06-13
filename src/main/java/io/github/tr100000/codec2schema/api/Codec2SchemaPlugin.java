package io.github.tr100000.codec2schema.api;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface Codec2SchemaPlugin {
    default void earlyRegisterHandlers() {}
    default void registerHandlers() {}
    default void generateSchemas(SchemaExporter exporter) {}

    default boolean shouldRun() {
        return true;
    }

    default @Nullable Identifier getId() {
        return null;
    }
}
