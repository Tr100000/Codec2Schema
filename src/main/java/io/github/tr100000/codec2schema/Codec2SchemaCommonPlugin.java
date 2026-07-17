package io.github.tr100000.codec2schema;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.SchemaExporter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.advancements.Advancement;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.ChatType;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.tags.TagFile;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.entity.animal.chicken.ChickenSoundVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.cow.CowSoundVariant;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.feline.CatSoundVariant;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.animal.pig.PigSoundVariant;
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
import net.minecraft.world.item.slot.SlotSources;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
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
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.timeline.Timeline;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Codec2SchemaCommonPlugin implements Codec2SchemaPlugin {
    private final ExportedSchemasTracker exportedSchemasTracker = new ExportedSchemasTracker();

    @Override
    public void generateSchemas(SchemaExporter exporter) {
        exportedSchemasTracker.clear();

        // https://minecraft.wiki/w/Data_pack#Folder_structure
        exportDataCodec(exporter, Advancement.CODEC, "advancement.json");
        exportDataCodec(exporter, BannerPattern.DIRECT_CODEC, "banner_pattern.json");
        exportDataCodec(exporter, CatSoundVariant.DIRECT_CODEC, "cat_sound_variant.json");
        exportDataCodec(exporter, CatVariant.DIRECT_CODEC, "cat_variant.json");
        exportDataCodec(exporter, ChatType.DIRECT_CODEC, "chat_type.json");
        exportDataCodec(exporter, ChickenSoundVariant.DIRECT_CODEC, "chicken_sound_variant.json");
        exportDataCodec(exporter, ChickenVariant.DIRECT_CODEC, "chicken_variant.json");
        exportDataCodec(exporter, CowSoundVariant.DIRECT_CODEC, "cow_sound_variant.json");
        exportDataCodec(exporter, CowVariant.DIRECT_CODEC, "cow_variant.json");
        exportDataCodec(exporter, DamageType.DIRECT_CODEC, "damage_type.json");
        exportDataCodec(exporter, DecoratedPotPattern.CODEC, "decorated_pot_pattern.json");
        exportDataCodec(exporter, Dialog.DIRECT_CODEC, "dialog.json");
        exportDataCodec(exporter, LevelStem.CODEC, "dimension.json");
        exportDataCodec(exporter, DimensionType.DIRECT_CODEC, "dimension_type.json");
        exportDataCodec(exporter, Enchantment.DIRECT_CODEC, "enchantment.json");
        exportDataCodec(exporter, EnchantmentProvider.DIRECT_CODEC, "enchantment_provider.json");
        exportDataCodec(exporter, FrogVariant.DIRECT_CODEC, "frog_variant.json");
        exportDataCodec(exporter, Instrument.DIRECT_CODEC, "instrument.json");
        exportDataCodec(exporter, LootItemFunctions.DIRECT_CODEC, "item_modifier.json");
        exportDataCodec(exporter, JukeboxSong.DIRECT_CODEC, "jukebox_song.json");
        exportDataCodec(exporter, LootTable.DIRECT_CODEC, "loot_table.json");
        exportDataCodec(exporter, NumberProviders.DIRECT_CODEC, "number_provider.json");
        exportDataCodec(exporter, PaintingVariant.DIRECT_CODEC, "painting_variant.json");
        exportDataCodec(exporter, PigSoundVariant.DIRECT_CODEC, "pig_sound_variant.json");
        exportDataCodec(exporter, PigVariant.DIRECT_CODEC, "pig_variant.json");
        exportDataCodec(exporter, LootItemCondition.DIRECT_CODEC, "predicate.json");
        exportDataCodec(exporter, Recipe.CODEC, "recipe.json");
        exportDataCodec(exporter, SlotSources.DIRECT_CODEC, "slot_source.json");
        exportDataCodec(exporter, SulfurCubeArchetype.DIRECT_CODEC, "sulfur_cube_archetype.json");
        exportDataCodec(exporter, TagFile.CODEC, "tags.json");
        exportDataCodec(exporter, TestEnvironmentDefinition.DIRECT_CODEC, "test_environment.json");
        exportDataCodec(exporter, GameTestInstance.DIRECT_CODEC, "test_instance.json");
        exportDataCodec(exporter, Timeline.DIRECT_CODEC, "timeline.json");
        exportDataCodec(exporter, TradeSet.CODEC, "trade_set.json");
        exportDataCodec(exporter, TrialSpawnerConfig.DIRECT_CODEC, "trial_spawner.json");
        exportDataCodec(exporter, TrimMaterial.DIRECT_CODEC, "trim_material.json");
        exportDataCodec(exporter, TrimPattern.DIRECT_CODEC, "trim_pattern.json");
        exportDataCodec(exporter, VillagerTrade.CODEC, "villager_trade.json");
        exportDataCodec(exporter, WolfSoundVariant.DIRECT_CODEC, "wolf_sound_variant.json");
        exportDataCodec(exporter, WolfVariant.DIRECT_CODEC, "wolf_variant.json");
        exportDataCodec(exporter, WorldClock.DIRECT_CODEC, "world_clock.json");
        exportDataCodec(exporter, ZombieNautilusVariant.DIRECT_CODEC, "zombie_nautilus_variant.json");

        exportDataWorldgenCodec(exporter, Biome.DIRECT_CODEC, "biome.json");
        exportDataWorldgenCodec(exporter, WorldCarver.DIRECT_CODEC, "carver.json");
        exportDataWorldgenCodec(exporter, DensityFunctions.DIRECT_CODEC, "density_function.json");
        exportDataWorldgenCodec(exporter, SurfaceRules.RuleSource.DIRECT_CODEC, "material_rule.json");
        exportDataWorldgenCodec(exporter, SurfaceRules.ConditionSource.DIRECT_CODEC, "material_condition.json");
        exportDataWorldgenCodec(exporter, Feature.DIRECT_CODEC, "feature.json");
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

        Set<RegistryDataLoader.RegistryData<?>> registries = new ObjectOpenHashSet<>();
        registries.addAll(RegistryDataLoader.WORLD_REGISTRIES);
        registries.addAll(RegistryDataLoader.DIMENSION_REGISTRIES);
        registries.addAll(RegistryDataLoader.RELOADABLE_REGISTRIES);
        checkExportedAll(registries);
    }

    private void exportDataCodec(SchemaExporter exporter, Codec<?> codec, String path) {
        exporter.accept(codec, "data", path);
        exportedSchemasTracker.add(codec);
    }

    private void exportDataWorldgenCodec(SchemaExporter exporter, Codec<?> codec, String path) {
        exporter.accept(codec, "data", "worldgen", path);
        exportedSchemasTracker.add(codec);
    }

    private void checkExportedAll(Collection<RegistryDataLoader.RegistryData<?>> registryList) {
        registryList.forEach(r -> exportedSchemasTracker.checkHas(r.elementCodec(), r.key().identifier().toString()));
    }

    private record ExportedSchemasTracker(List<Codec<?>> codecs) {
        public ExportedSchemasTracker() {
            this(new ObjectArrayList<>());
        }

        public void clear() {
            codecs.clear();
        }

        public void add(Codec<?> codec) {
            codecs.add(codec);
        }

        public void checkHas(Codec<?> other, String name) {
            if (!codecs.contains(other)) {
                Codec2Schema.LOGGER.warn("Didn't export {}", name);
            }
        }
    }
}
