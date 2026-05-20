package io.github.tr100000.codec2schema;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ModUtils {
    private ModUtils() {}

    public static boolean hasFabricModelLoadingApi() {
        return FabricLoader.getInstance().isModLoaded("fabric-model-loading-api-v1");
    }
}
