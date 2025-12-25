package io.github.tr100000.codec2schema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandlerRegistry;
import io.github.tr100000.codec2schema.api.SchemaExporter;
import io.github.tr100000.codec2schema.api.SchemaGenerateEntrypoint;
import io.github.tr100000.codec2schema.impl.DispatchedMapCodecHandler;
import io.github.tr100000.codec2schema.impl.EitherCodecHandler;
import io.github.tr100000.codec2schema.impl.HolderSetCodecHandler;
import io.github.tr100000.codec2schema.impl.ListCodecHandler;
import io.github.tr100000.codec2schema.impl.NumberCodecHandler;
import io.github.tr100000.codec2schema.impl.NumberStreamCodecHandler;
import io.github.tr100000.codec2schema.impl.PassthroughCodecHandler;
import io.github.tr100000.codec2schema.impl.PrimitiveCodecHandler;
import io.github.tr100000.codec2schema.impl.RegistryFileCodecHandler;
import io.github.tr100000.codec2schema.impl.RegistryFixedCodecHandler;
import io.github.tr100000.codec2schema.impl.StrictUnboundedMapCodecHandler;
import io.github.tr100000.codec2schema.impl.UnboundedMapCodecHandler;
import io.github.tr100000.codec2schema.impl.XorCodecHandler;
import io.github.tr100000.codec2schema.impl.map.MapCodecCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.DataComponentPatchCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.DataComponentTypeCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.DataComponentValueMapCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.IdentifierCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.SinglePoolElementTemplateCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.WrappedRestrictedComponentCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.WrappedStateHolderCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.WrappedTypedEntityDataCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.CodecWithValuesHandler;
import io.github.tr100000.codec2schema.impl.wrapped.RecursiveCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.StringEnumCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedUnitCodecHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.Advancement;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Codec2Schema implements ModInitializer, SchemaGenerateEntrypoint {
    public static final String MODID = "codec2schema";
    public static final Logger LOGGER = LoggerFactory.getLogger("Codec2Schema");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Path EXPORT_ROOT_DIR = FabricLoader.getInstance().getGameDir().resolve("export");

    @Override
    public void onInitialize() {
        CodecHandlerRegistry.register(DataComponentPatchCodecHandler::predicate, DataComponentPatchCodecHandler::new);
        CodecHandlerRegistry.register(DataComponentTypeCodecHandler::predicate, DataComponentTypeCodecHandler::new);
        CodecHandlerRegistry.register(DataComponentValueMapCodecHandler::predicate, DataComponentValueMapCodecHandler::new);
        CodecHandlerRegistry.register(IdentifierCodecHandler::predicate, IdentifierCodecHandler::new);
        CodecHandlerRegistry.register(SinglePoolElementTemplateCodecHandler::predicate, SinglePoolElementTemplateCodecHandler::new);
        CodecHandlerRegistry.register(WrappedRestrictedComponentCodecHandler::predicate, WrappedRestrictedComponentCodecHandler::new);
        CodecHandlerRegistry.register(WrappedStateHolderCodecHandler::predicate, WrappedStateHolderCodecHandler::new);
        CodecHandlerRegistry.register(WrappedTypedEntityDataCodecHandler::predicate, WrappedTypedEntityDataCodecHandler::new);

        CodecHandlerRegistry.register(PrimitiveCodecHandler::predicate, PrimitiveCodecHandler::createHandler);
        CodecHandlerRegistry.register(NumberStreamCodecHandler::predicate, NumberStreamCodecHandler::createHandler);
        CodecHandlerRegistry.register(NumberCodecHandler::predicate, NumberCodecHandler::createHandler);
        CodecHandlerRegistry.register(PassthroughCodecHandler::predicate, PassthroughCodecHandler::new);
        CodecHandlerRegistry.register(WrappedUnitCodecHandler::predicate, WrappedUnitCodecHandler::new);
        CodecHandlerRegistry.register(EitherCodecHandler::predicate, EitherCodecHandler::new);
        CodecHandlerRegistry.register(XorCodecHandler::predicate, XorCodecHandler::new);
        CodecHandlerRegistry.register(ListCodecHandler::predicate, ListCodecHandler::new);
        CodecHandlerRegistry.register(UnboundedMapCodecHandler::predicate, UnboundedMapCodecHandler::new);
        CodecHandlerRegistry.register(DispatchedMapCodecHandler::predicate, DispatchedMapCodecHandler::createHandler);
        CodecHandlerRegistry.register(RecursiveCodecHandler::predicate, RecursiveCodecHandler::new);
        CodecHandlerRegistry.register(WrappedCodecHandler::predicate, WrappedCodecHandler::new);
        CodecHandlerRegistry.register(MapCodecCodecHandler::predicate, MapCodecCodecHandler::new);
        CodecHandlerRegistry.register(HolderSetCodecHandler::predicate, HolderSetCodecHandler::new);

        CodecHandlerRegistry.register(StrictUnboundedMapCodecHandler::predicate, StrictUnboundedMapCodecHandler::new);
        CodecHandlerRegistry.register(RegistryFixedCodecHandler::predicate, RegistryFixedCodecHandler::new);
        CodecHandlerRegistry.register(RegistryFileCodecHandler::predicate, RegistryFileCodecHandler::new);
        CodecHandlerRegistry.register(CodecWithValuesHandler::predicate, CodecWithValuesHandler::new);
        CodecHandlerRegistry.register(StringEnumCodecHandler::predicate, StringEnumCodecHandler::createHandler);
        CodecHandlerRegistry.register(NumberCodecHandler::rangePredicate, NumberCodecHandler::createRangedHandler);

        FabricLoader.getInstance().invokeEntrypoints("codec2schema:generate", SchemaGenerateEntrypoint.class, entrypoint -> {
            SchemaExporter exporter = new SchemaExporter();
            entrypoint.generate(exporter);
        });
    }

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
    }

    private void exportDataCodec(SchemaExporter exporter, Codec<?> codec, String path) {
        exporter.accept(codec, "data", path);
    }

    private void exportDataWorldgenCodec(SchemaExporter exporter, Codec<?> codec, String path) {
        exporter.accept(codec, "data", "worldgen", path);
    }
}
