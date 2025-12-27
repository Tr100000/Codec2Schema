package io.github.tr100000.codec2schema.api;

@FunctionalInterface
public interface SchemaGenerationEntrypoint {
    void generate(SchemaExporter exporter);
}
