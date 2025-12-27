package io.github.tr100000.codec2schema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.tr100000.codec2schema.api.CodecHandlerRegistrationEntrypoint;
import io.github.tr100000.codec2schema.api.CodecHandlerRegistry;
import io.github.tr100000.codec2schema.api.SchemaExporter;
import io.github.tr100000.codec2schema.api.SchemaGenerationEntrypoint;
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
import io.github.tr100000.codec2schema.impl.wrapped.WrappedRangedNumberCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedUnitCodecHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Codec2Schema implements ModInitializer {
    public static final String MODID = "codec2schema";
    public static final Logger LOGGER = LoggerFactory.getLogger("Codec2Schema");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Path EXPORT_ROOT_DIR = FabricLoader.getInstance().getGameDir().resolve("codec2schema");

    @Override
    public void onInitialize() {
        FabricLoader.getInstance().invokeEntrypoints("codec2schema:register_early", CodecHandlerRegistrationEntrypoint.class, CodecHandlerRegistrationEntrypoint::register);

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
        CodecHandlerRegistry.register(WrappedRangedNumberCodecHandler::predicate, WrappedRangedNumberCodecHandler::new);
        CodecHandlerRegistry.register(PassthroughCodecHandler::predicate, PassthroughCodecHandler::new);
        CodecHandlerRegistry.register(WrappedUnitCodecHandler::predicate, WrappedUnitCodecHandler::new);
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
        CodecHandlerRegistry.register(StringEnumCodecHandler::predicate, StringEnumCodecHandler::new);
        CodecHandlerRegistry.register(CodecWithValuesHandler::predicate, CodecWithValuesHandler::new);
        CodecHandlerRegistry.register(NumberCodecHandler::rangePredicate, NumberCodecHandler::createRangedHandler);

        CodecHandlerRegistry.register(RecursiveCodecHandler::predicate, RecursiveCodecHandler::new);
        CodecHandlerRegistry.register(WrappedCodecHandler::predicate, WrappedCodecHandler::new);

        FabricLoader.getInstance().invokeEntrypoints("codec2schema:register", CodecHandlerRegistrationEntrypoint.class, CodecHandlerRegistrationEntrypoint::register);

        long startTimeMillis = System.currentTimeMillis();
        FabricLoader.getInstance().invokeEntrypoints("codec2schema:generate", SchemaGenerationEntrypoint.class, entrypoint -> {
            SchemaExporter exporter = new SchemaExporter();
            entrypoint.generate(exporter);
        });
        LOGGER.info("Finished all schema generation in {}ms", System.currentTimeMillis() - startTimeMillis);
    }
}
