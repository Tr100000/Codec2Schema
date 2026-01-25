package io.github.tr100000.codec2schema.compat.fabric;

import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.CodecHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;

public class FabricCompat implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
        if (FabricLoader.getInstance().isModLoaded("fabric-dimensions-v1")) {
            CodecHandlerRegistry.register(FailSoftMapCodecHandler::predicate, FailSoftMapCodecHandler::new);
        }
    }
}
