package io.github.tr100000.codec2schema.compat.fabric;

import io.github.tr100000.codec2schema.ModUtils;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class FabricCompatClient implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
        if (ModUtils.hasFabricModelLoadingApi()) {
//            MapCodecHandlerRegistry.register(KeyExistsCodecHandler::predicate, KeyExistsCodecHandler::new);
        }
    }

    @Override
    public boolean shouldRun() {
        return ModUtils.hasFabricModelLoadingApi();
    }

    @Override
    public @Nullable Identifier getId() {
        return ModUtils.id("client/fabric_compat");
    }
}
