package io.github.tr100000.codec2schema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.CodecHandlerRegistry;
import io.github.tr100000.codec2schema.api.CodecValueLister;
import io.github.tr100000.codec2schema.api.MapCodecHandlerRegistry;
import io.github.tr100000.codec2schema.api.SchemaExporter;
import io.github.tr100000.codec2schema.impl.CodecWithValuePairsLister;
import io.github.tr100000.codec2schema.impl.CompoundListCodecHandler;
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
import io.github.tr100000.codec2schema.impl.map.ComponentSerializationCodecHandler;
import io.github.tr100000.codec2schema.impl.map.EitherMapCodecHandler;
import io.github.tr100000.codec2schema.impl.map.KeyDispatchCodecHandler;
import io.github.tr100000.codec2schema.impl.map.MapCodecCodecHandler;
import io.github.tr100000.codec2schema.impl.map.OptionalFieldCodecHandler;
import io.github.tr100000.codec2schema.impl.map.PairMapCodecHandler;
import io.github.tr100000.codec2schema.impl.map.SimpleMapCodecHandler;
import io.github.tr100000.codec2schema.impl.map.WrappedAssumeMapUnsafeMapCodecHandler;
import io.github.tr100000.codec2schema.impl.map.WrappedDispatchOptionalValueMapCodecHandler;
import io.github.tr100000.codec2schema.impl.map.WrappedFieldMapCodecHandler;
import io.github.tr100000.codec2schema.impl.map.WrappedUnitMapCodecHandler;
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
import io.github.tr100000.codec2schema.impl.wrapped.WrappedCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedConstrainedStringCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedRangedNumberCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedUnitCodecHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class Codec2Schema {
    private Codec2Schema() {}

    public static final String MODID = "codec2schema";
    public static final Logger LOGGER = LoggerFactory.getLogger("Codec2Schema");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Path EXPORT_ROOT_DIR = FabricLoader.getInstance().getGameDir().resolve(MODID);

    public static String version() {
        return FabricLoader.getInstance().getModContainer(MODID).orElseThrow().getMetadata().getVersion().getFriendlyString();
    }

    private static String getEntrypointKey(PluginSide side) {
        return switch (side) {
            case MAIN -> "codec2schema:main";
            case CLIENT -> "codec2schema:client";
        };
    }

    private static List<Codec2SchemaPlugin> getFilteredPlugins(PluginSide... sides) {
        List<Codec2SchemaPlugin> plugins = new ObjectArrayList<>();
        for (PluginSide side : sides) {
            List<Codec2SchemaPlugin> sidedPlugins = FabricLoader.getInstance().getEntrypointContainers(getEntrypointKey(side), Codec2SchemaPlugin.class)
                    .stream()
                    .filter(c -> Codec2SchemaConfig.INSTANCE.shouldRunPlugin(c.getProvider().getMetadata().getId(), side))
                    .map(EntrypointContainer::getEntrypoint)
                    .toList();
            plugins.addAll(sidedPlugins);
        }
        return plugins;
    }

    @ApiStatus.Internal
    public static void registerHandlers(PluginSide... sides) {
        List<Codec2SchemaPlugin> plugins = getFilteredPlugins(sides);

        plugins.forEach(Codec2SchemaPlugin::earlyRegisterHandlers);

        CodecValueLister.LISTERS.add(new CodecWithValuePairsLister());

        LOGGER.info("Registering codec handlers");

        registerSpecificCodecHandlers();
        registerBaseCodecHandlers();
        registerSchemaModifiers();

        registerSpecificMapCodecHandlers();
        registerBaseMapCodecHandlers();

        plugins.forEach(Codec2SchemaPlugin::registerHandlers);
    }

    private static void registerSpecificCodecHandlers() {
        CodecHandlerRegistry.register(DataComponentPatchCodecHandler::predicate, DataComponentPatchCodecHandler::new);
        CodecHandlerRegistry.register(DataComponentTypeCodecHandler::predicate, DataComponentTypeCodecHandler::new);
        CodecHandlerRegistry.register(DataComponentValueMapCodecHandler::predicate, DataComponentValueMapCodecHandler::new);
        CodecHandlerRegistry.register(IdentifierCodecHandler::predicate, IdentifierCodecHandler::new);
        CodecHandlerRegistry.register(SinglePoolElementTemplateCodecHandler::predicate, SinglePoolElementTemplateCodecHandler::new);
        CodecHandlerRegistry.register(WrappedRestrictedComponentCodecHandler::predicate, WrappedRestrictedComponentCodecHandler::new);
        CodecHandlerRegistry.register(WrappedStateHolderCodecHandler::predicate, WrappedStateHolderCodecHandler::new);
        CodecHandlerRegistry.register(WrappedTypedEntityDataCodecHandler::predicate, WrappedTypedEntityDataCodecHandler::new);
    }

    private static void registerBaseCodecHandlers() {
        CodecHandlerRegistry.register(PrimitiveCodecHandler::predicate, PrimitiveCodecHandler::createHandler);
        CodecHandlerRegistry.register(NumberStreamCodecHandler::predicate, NumberStreamCodecHandler::createHandler);
        CodecHandlerRegistry.register(NumberCodecHandler::predicate, NumberCodecHandler::createHandler);
        CodecHandlerRegistry.register(WrappedRangedNumberCodecHandler::predicate, WrappedRangedNumberCodecHandler::new);
        CodecHandlerRegistry.register(WrappedConstrainedStringCodecHandler::predicate, WrappedConstrainedStringCodecHandler::new);
        CodecHandlerRegistry.register(PassthroughCodecHandler::predicate, PassthroughCodecHandler::new);
        CodecHandlerRegistry.register(WrappedUnitCodecHandler::predicate, WrappedUnitCodecHandler::new);
        CodecHandlerRegistry.register(CompoundListCodecHandler::predicate, CompoundListCodecHandler::new);
        CodecHandlerRegistry.register(EitherCodecHandler::predicate, EitherCodecHandler::new);
        CodecHandlerRegistry.register(XorCodecHandler::predicate, XorCodecHandler::new);
        CodecHandlerRegistry.register(ListCodecHandler::predicate, ListCodecHandler::new);
        CodecHandlerRegistry.register(UnboundedMapCodecHandler::predicate, UnboundedMapCodecHandler::new);
        CodecHandlerRegistry.register(DispatchedMapCodecHandler::predicate, DispatchedMapCodecHandler::createHandler);
        CodecHandlerRegistry.register(MapCodecCodecHandler::predicate, MapCodecCodecHandler::new);
        CodecHandlerRegistry.register(HolderSetCodecHandler::predicate, HolderSetCodecHandler::new);

        CodecHandlerRegistry.register(StrictUnboundedMapCodecHandler::predicate, StrictUnboundedMapCodecHandler::new);
        CodecHandlerRegistry.register(RegistryFixedCodecHandler::predicate, RegistryFixedCodecHandler::new);
        CodecHandlerRegistry.register(RegistryFileCodecHandler::predicate, RegistryFileCodecHandler::new);
        CodecHandlerRegistry.register(CodecWithValuesHandler::predicate, CodecWithValuesHandler::new);
        CodecHandlerRegistry.register(NumberCodecHandler::rangePredicate, NumberCodecHandler::createRangedHandler);

        CodecHandlerRegistry.register(RecursiveCodecHandler::predicate, RecursiveCodecHandler::new);
        CodecHandlerRegistry.register(WrappedCodecHandler::predicate, WrappedCodecHandler::new);
    }

    private static void registerSpecificMapCodecHandlers() {
        MapCodecHandlerRegistry.register(ComponentSerializationCodecHandler::predicate, ComponentSerializationCodecHandler::new);
        MapCodecHandlerRegistry.register(WrappedUnitMapCodecHandler::predicate, WrappedUnitMapCodecHandler::new);
    }

    private static void registerBaseMapCodecHandlers() {
        MapCodecHandlerRegistry.register(WrappedFieldMapCodecHandler::predicate, WrappedFieldMapCodecHandler::new);
        MapCodecHandlerRegistry.register(OptionalFieldCodecHandler::predicate, OptionalFieldCodecHandler::new);
        MapCodecHandlerRegistry.register(SimpleMapCodecHandler::predicate, SimpleMapCodecHandler::create);
        MapCodecHandlerRegistry.register(EitherMapCodecHandler::predicate, EitherMapCodecHandler::new);
        MapCodecHandlerRegistry.register(PairMapCodecHandler::predicate, PairMapCodecHandler::new);

        MapCodecHandlerRegistry.register(KeyDispatchCodecHandler::predicate, KeyDispatchCodecHandler::create);
        MapCodecHandlerRegistry.register(WrappedDispatchOptionalValueMapCodecHandler::predicate, WrappedDispatchOptionalValueMapCodecHandler::create);

        MapCodecHandlerRegistry.register(WrappedAssumeMapUnsafeMapCodecHandler::predicate, WrappedAssumeMapUnsafeMapCodecHandler::new);
    }

    private static void registerSchemaModifiers() {
        // nothing
    }

    @ApiStatus.Internal
    public static void afterBootstrap() {
        Codec2SchemaConfig.load();

        ClientDelegate.INSTANCE.registerHandlers();

        try {
            FileUtils.deleteDirectory(EXPORT_ROOT_DIR.toFile());
        }
        catch (IOException e) {
            LOGGER.warn("Failed to delete previously generated schemas", e);
        }

        LOGGER.info("Starting schema generation");
        long startTimeMillis = System.currentTimeMillis();
        ClientDelegate.INSTANCE.generateSchemas();
        LOGGER.info("Finished all schema generation in {}ms", System.currentTimeMillis() - startTimeMillis);
    }

    public static void generateSchemas(PluginSide... entrypointKeys) {
        SchemaExporter exporter = new SchemaExporter();
        getFilteredPlugins(entrypointKeys).forEach(plugin -> {
            plugin.generateSchemas(exporter);
            exporter.clearOptions();
        });
    }
}
