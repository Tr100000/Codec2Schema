package io.github.tr100000.codec2schema.compat.fabric;

import io.github.tr100000.codec2schema.ModUtils;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.MapCodecHandlerRegistry;

public class FabricCompatClient implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
        if (ModUtils.hasFabricModelLoadingApi()) {
            MapCodecHandlerRegistry.register(KeyExistsCodecHandler::predicate, KeyExistsCodecHandler::new);
        }
    }
}
