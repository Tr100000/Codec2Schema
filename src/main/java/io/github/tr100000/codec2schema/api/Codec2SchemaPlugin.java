package io.github.tr100000.codec2schema.api;

public interface Codec2SchemaPlugin {
    default void earlyRegisterHandlers() {}
    default void registerHandlers() {}
    default void generateSchemas(SchemaExporter exporter) {}
}
