package io.github.tr100000.codec2schema;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.SchemaExporter;
import io.github.tr100000.codec2schema.api.SchemaGenerationEntrypoint;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.PeriodicNotificationManager;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.renderer.PostChainConfig;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.client.resources.WaypointStyle;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.tags.TagFile;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.timeline.Timeline;

public class Codec2SchemaGeneration implements SchemaGenerationEntrypoint {
    @Override
    public void generate(SchemaExporter exporter) {
        // https://minecraft.wiki/w/Data_pack#Folder_structure
        exportDataCodec(exporter, TagFile.CODEC, "tags.json");
        exportDataCodec(exporter, Advancement.CODEC, "advancement.json");
        exportDataCodec(exporter, BannerPattern.DIRECT_CODEC, "banner_pattern.json");
        exportDataCodec(exporter, CatVariant.DIRECT_CODEC, "cat_variant.json");
        exportDataCodec(exporter, ChatType.DIRECT_CODEC, "chat_type.json");
        exportDataCodec(exporter, ChickenVariant.DIRECT_CODEC, "chicken_variant.json");
        exportDataCodec(exporter, CowVariant.DIRECT_CODEC, "cow_variant.json");
        exportDataCodec(exporter, Dialog.DIRECT_CODEC, "dialog.json");
        exportDataCodec(exporter, LevelStem.CODEC, "dimension.json");
        exportDataCodec(exporter, DimensionType.DIRECT_CODEC, "dimension_type.json");
        exportDataCodec(exporter, Enchantment.DIRECT_CODEC, "enchantment.json");
        exportDataCodec(exporter, EnchantmentProvider.DIRECT_CODEC, "enchantment_provider.json");
        exportDataCodec(exporter, FrogVariant.DIRECT_CODEC, "frog_variant.json");
        exportDataCodec(exporter, Instrument.DIRECT_CODEC, "instrument.json");
        exportDataCodec(exporter, LootItemFunctions.ROOT_CODEC, "item_modifier.json");
        exportDataCodec(exporter, JukeboxSong.DIRECT_CODEC, "jukebox_song.json");
        exportDataCodec(exporter, LootTable.DIRECT_CODEC, "loot_table.json");
        exportDataCodec(exporter, PaintingVariant.DIRECT_CODEC, "painting_variant.json");
        exportDataCodec(exporter, PigVariant.DIRECT_CODEC, "pig_variant.json");
        exportDataCodec(exporter, LootItemCondition.DIRECT_CODEC, "predicate.json");
        exportDataCodec(exporter, Recipe.CODEC, "recipe.json");
        exportDataCodec(exporter, TestEnvironmentDefinition.DIRECT_CODEC, "test_environment.json");
        exportDataCodec(exporter, GameTestInstance.DIRECT_CODEC, "test_instance.json");
        exportDataCodec(exporter, Timeline.DIRECT_CODEC, "timeline.json");
        exportDataCodec(exporter, TrialSpawnerConfig.DIRECT_CODEC, "trial_spawner.json");
        exportDataCodec(exporter, TrimMaterial.DIRECT_CODEC, "trim_material.json");
        exportDataCodec(exporter, TrimPattern.DIRECT_CODEC, "trim_pattern.json");
        exportDataCodec(exporter, WolfSoundVariant.DIRECT_CODEC, "wolf_sound_variant.json");
        exportDataCodec(exporter, WolfVariant.DIRECT_CODEC, "wolf_variant.json");
        exportDataWorldgenCodec(exporter, Biome.DIRECT_CODEC, "biome.json");
        exportDataWorldgenCodec(exporter, ConfiguredWorldCarver.DIRECT_CODEC, "configured_carver.json");
        exportDataWorldgenCodec(exporter, ConfiguredFeature.DIRECT_CODEC, "configured_feature.json");
        exportDataWorldgenCodec(exporter, DensityFunction.DIRECT_CODEC, "density_function.json");
        exportDataWorldgenCodec(exporter, NormalNoise.NoiseParameters.DIRECT_CODEC, "noise.json");
        exportDataWorldgenCodec(exporter, NoiseGeneratorSettings.DIRECT_CODEC, "noise_settings.json");
        exportDataWorldgenCodec(exporter, PlacedFeature.DIRECT_CODEC, "placed_feature.json");
        exportDataWorldgenCodec(exporter, StructureProcessorType.DIRECT_CODEC, "processor_list.json");
        exportDataWorldgenCodec(exporter, Structure.DIRECT_CODEC, "structure.json");
        exportDataWorldgenCodec(exporter, StructureSet.DIRECT_CODEC, "structure_set.json");
        exportDataWorldgenCodec(exporter, StructureTemplatePool.DIRECT_CODEC, "template_pool.json");
        exportDataWorldgenCodec(exporter, WorldPreset.DIRECT_CODEC, "world_preset.json");
        exportDataWorldgenCodec(exporter, FlatLevelGeneratorPreset.DIRECT_CODEC, "flat_level_generator_preset.json");
        exportDataWorldgenCodec(exporter, MultiNoiseBiomeSourceParameterList.DIRECT_CODEC, "multi_noise_biome_source_parameter_list.json");

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            // https://minecraft.wiki/w/Resource_pack#Directory_structure
            exportResourceCodec(exporter, SpriteSources.FILE_CODEC, "atlases.json");
            exportResourceCodec(exporter, BlockModelDefinition.CODEC, "blockstates.json");
            exportResourceCodec(exporter, EquipmentClientInfo.CODEC, "equipment.json");
            exportResourceCodec(exporter, FontManager.FontDefinitionFile.CODEC, "font.json");
            exportResourceCodec(exporter, ClientItem.CODEC, "items.json");
//            exportResourceCodec(exporter, , "lang.json");
//            exportResourceCodec(exporter, , "models.json");
//            exportResourceCodec(exporter, , "particles.json");
            exportResourceCodec(exporter, PostChainConfig.CODEC, "post_effect.json");
            exportResourceCodec(exporter, WaypointStyle.CODEC, "waypoint_style.json");
            exportResourceCodec(exporter, PeriodicNotificationManager.CODEC, "regional_compliance.json");
        }
    }

    private void exportDataCodec(SchemaExporter exporter, Codec<?> codec, String path) {
        exporter.accept(codec, "data", path);
    }

    private void exportDataWorldgenCodec(SchemaExporter exporter, Codec<?> codec, String path) {
        exporter.accept(codec, "data", "worldgen", path);
    }

    private void exportResourceCodec(SchemaExporter exporter, Codec<?> codec, String path) {
        exporter.accept(codec, "resources", path);
    }
}
