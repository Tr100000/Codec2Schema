package io.github.tr100000.codec2schema.compat.fabric;

import io.github.tr100000.codec2schema.api.CodecHandlerRegistrationEntrypoint;
import io.github.tr100000.codec2schema.api.CodecHandlerRegistry;
import io.github.tr100000.codec2schema.api.MapCodecHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;

public class FabricCompat implements CodecHandlerRegistrationEntrypoint {
    @Override
    public void register() {
        if (FabricLoader.getInstance().isModLoaded("fabric-dimensions-v1")) {
            CodecHandlerRegistry.register(FailSoftMapCodecHandler::predicate, FailSoftMapCodecHandler::new);
        }
        if (FabricLoader.getInstance().isModLoaded("fabric-model-loading-api-v1")) {
            MapCodecHandlerRegistry.register(KeyExistsCodecHandler::predicate, KeyExistsCodecHandler::new);
        }
    }
}
