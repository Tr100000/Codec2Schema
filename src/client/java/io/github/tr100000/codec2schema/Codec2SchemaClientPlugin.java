package io.github.tr100000.codec2schema;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.SchemaExporter;
import net.minecraft.client.PeriodicNotificationManager;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.renderer.PostChainConfig;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.client.resources.WaypointStyle;
import net.minecraft.client.resources.model.EquipmentClientInfo;

public class Codec2SchemaClientPlugin implements Codec2SchemaPlugin {
    @Override
    public void generateSchemas(SchemaExporter exporter) {
        // https://minecraft.wiki/w/Resource_pack#Directory_structure
        exportResourceCodec(exporter, SpriteSources.FILE_CODEC, "atlases.json");
        exportResourceCodec(exporter, BlockStateModelDispatcher.CODEC, "blockstates.json");
        exportResourceCodec(exporter, EquipmentClientInfo.CODEC, "equipment.json");
        exportResourceCodec(exporter, FontManager.FontDefinitionFile.CODEC, "font.json");
        exportResourceCodec(exporter, ClientItem.CODEC, "items.json");
//            exportResourceCodec(exporter, , "lang.json");
//            exportResourceCodec(exporter, , "models.json");
//            exportResourceCodec(exporter, , "particles.json");
        exportResourceCodec(exporter, PostChainConfig.CODEC, "post_effect.json");
        exportResourceCodec(exporter, WaypointStyle.CODEC, "waypoint_style.json");
        exportResourceCodec(exporter, PeriodicNotificationManager.CODEC, "regional_compliancies.json");
    }

    private void exportResourceCodec(SchemaExporter exporter, Codec<?> codec, String path) {
        exporter.accept(codec, "resources", path);
    }
}
