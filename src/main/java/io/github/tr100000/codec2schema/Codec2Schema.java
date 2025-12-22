package io.github.tr100000.codec2schema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandlerRegistry;
import io.github.tr100000.codec2schema.api.SchemaContext;
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
import io.github.tr100000.codec2schema.impl.UnboundedMapCodecHandler;
import io.github.tr100000.codec2schema.impl.XorCodecHandler;
import io.github.tr100000.codec2schema.impl.map.MapCodecCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.ComponentCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.DataComponentPatchCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.WrappedRestrictedComponentCodecHandler;
import io.github.tr100000.codec2schema.impl.specific.WrappedTypedEntityDataCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.CodecWithValuesHandler;
import io.github.tr100000.codec2schema.impl.wrapped.RecursiveCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.StringEnumCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedCodecHandler;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedUnitCodecHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Codec2Schema implements ModInitializer {
	public static final String MODID = "codec2schema";
    public static final Logger LOGGER = LoggerFactory.getLogger("Codec2Schema");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Path EXPORT_ROOT_DIR = FabricLoader.getInstance().getGameDir().resolve("export");

	@Override
	public void onInitialize() {
        CodecHandlerRegistry.register(ComponentCodecHandler::predicate, ComponentCodecHandler::new);
        CodecHandlerRegistry.register(DataComponentPatchCodecHandler::predicate, DataComponentPatchCodecHandler::new);
        CodecHandlerRegistry.register(WrappedRestrictedComponentCodecHandler::predicate, WrappedRestrictedComponentCodecHandler::new);
        CodecHandlerRegistry.register(WrappedTypedEntityDataCodecHandler::predicate, WrappedTypedEntityDataCodecHandler::new);

        CodecHandlerRegistry.register(PrimitiveCodecHandler::isPrimitiveCodec, PrimitiveCodecHandler::createHandler);
        CodecHandlerRegistry.register(NumberStreamCodecHandler::predicate, NumberStreamCodecHandler::createHandler);
        CodecHandlerRegistry.register(NumberCodecHandler::isNumberCodec, NumberCodecHandler::createHandler);
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

        CodecHandlerRegistry.register(RegistryFixedCodecHandler::predicate, RegistryFixedCodecHandler::new);
        CodecHandlerRegistry.register(RegistryFileCodecHandler::predicate, RegistryFileCodecHandler::new);
        CodecHandlerRegistry.register(CodecWithValuesHandler::predicate, CodecWithValuesHandler::new);
        CodecHandlerRegistry.register(StringEnumCodecHandler::predicate, StringEnumCodecHandler::createHandler);
        CodecHandlerRegistry.register(NumberCodecHandler::isRangedNumberCodec, NumberCodecHandler::createRangedHandler);

        export(RecipeSerializer.BLASTING_RECIPE.codec().codec(), EXPORT_ROOT_DIR.resolve("test.json"));
	}

    public static JsonObject convert(Codec<?> codec) {
        SchemaContext context = new SchemaContext();
        context.debugMode = true;
        JsonObject json = context.requestDefinition(codec);
        context.addDefinitions(json);
        json.addProperty("$schema", "https://json-schema.org/draft-07/schema");
        return json;
    }

    public static void export(Codec<?> codec, Path path) {
        try {
            Files.createDirectories(EXPORT_ROOT_DIR);
            Files.deleteIfExists(path);

            JsonObject json = convert(codec);
            Files.writeString(path, GSON.toJson(json));

            LOGGER.info("Exported codec to {}", path);
        }
        catch (IOException e) {
            LOGGER.warn("Failed to write codec schema to {}", path);
        }
    }
}
