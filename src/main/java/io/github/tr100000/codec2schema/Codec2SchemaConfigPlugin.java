package io.github.tr100000.codec2schema;

import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.SchemaExporter;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class Codec2SchemaConfigPlugin implements Codec2SchemaPlugin {
    @Override
    public void generateSchemas(SchemaExporter exporter) {
        exporter.accept(Codec2SchemaConfig.CODEC, "config", Codec2Schema.MODID + ".json");
    }

    @Override
    public @Nullable Identifier getId() {
        return ModUtils.id("common/config");
    }
}
