package io.github.tr100000.codec2schema.api;

@FunctionalInterface
@Deprecated(forRemoval = true)
public interface SchemaGenerationEntrypoint {
    void generate(SchemaExporter exporter);
}
