package io.github.tr100000.codec2schema;

import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import net.fabricmc.loader.api.ModContainer;

public record ExportContext(Codec2SchemaPlugin plugin, ModContainer mod, PluginSide side) {}
