package io.github.tr100000.codec2schema.compat.fabric;

import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;

public class FabricCompatClient implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
//        if (FabricLoader.getInstance().isModLoaded("fabric-model-loading-api-v1")) {
//            MapCodecHandlerRegistry.register(KeyExistsCodecHandler::predicate, KeyExistsCodecHandler::new);
//        }
    }
}
