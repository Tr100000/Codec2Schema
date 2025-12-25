package io.github.tr100000.codec2schema.api;

@FunctionalInterface
public interface SchemaGenerateEntrypoint {
    void generate(SchemaExporter exporter);
}
