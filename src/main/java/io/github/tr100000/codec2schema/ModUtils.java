package io.github.tr100000.codec2schema;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ModUtils {
    private ModUtils() {}

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Codec2Schema.MODID, path);
    }

    public static boolean hasFabricModelLoadingApi() {
        return FabricLoader.getInstance().isModLoaded("fabric-model-loading-api-v1");
    }

    public static boolean hasFabricDimensionsApi() {
        return FabricLoader.getInstance().isModLoaded("fabric-dimensions-v1");
    }

    public static boolean hasTrUtils() {
        return FabricLoader.getInstance().isModLoaded("trutils");
    }
}
