package io.github.tr100000.codec2schema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.tags.TagFile;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
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
        exporter.accept(TagFile.CODEC, "data", "tags.json");
        exporter.accept(Advancement.CODEC, "data", "advancement.json");
        exporter.accept(BannerPattern.DIRECT_CODEC, "data", "banner_pattern.json");
        exporter.accept(CatVariant.DIRECT_CODEC, "data", "cat_variant.json");
        exporter.accept(ChatType.DIRECT_CODEC, "data", "chat_type.json");
        exporter.accept(ChickenVariant.DIRECT_CODEC, "data", "chicken_variant.json");
        exporter.accept(CowVariant.DIRECT_CODEC, "data", "cow_variant.json");
        exporter.accept(Dialog.DIRECT_CODEC, "data", "dialog.json");
        exporter.accept(LevelStem.CODEC, "data", "dimension.json");
        exporter.accept(DimensionType.DIRECT_CODEC, "data", "dimension_type.json");
        exporter.accept(Enchantment.DIRECT_CODEC, "data", "enchantment.json");
        exporter.accept(EnchantmentProvider.DIRECT_CODEC, "data", "enchantment_provider.json");
        exporter.accept(FrogVariant.DIRECT_CODEC, "data", "frog_variant.json");
        exporter.accept(Instrument.DIRECT_CODEC, "data", "instrument.json");
        exporter.accept(LootItemFunctions.ROOT_CODEC, "data", "item_modifier.json");
        exporter.accept(JukeboxSong.DIRECT_CODEC, "data", "jukebox_song.json");
        exporter.accept(LootTable.DIRECT_CODEC, "data", "loot_table.json");
        exporter.accept(PaintingVariant.DIRECT_CODEC, "data", "painting_variant.json");
        exporter.accept(PigVariant.DIRECT_CODEC, "data", "pig_variant.json");
        exporter.accept(LootItemCondition.DIRECT_CODEC, "data", "predicate.json");
        exporter.accept(Recipe.CODEC, "data", "recipe.json");
    }
}
