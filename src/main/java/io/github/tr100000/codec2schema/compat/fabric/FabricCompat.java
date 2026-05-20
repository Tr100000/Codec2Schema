package io.github.tr100000.codec2schema.compat.fabric;

import io.github.tr100000.codec2schema.ModUtils;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.CodecHandlerRegistry;

public class FabricCompat implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
        if (ModUtils.hasFabricDimensionsApi()) {
            CodecHandlerRegistry.register(FailSoftMapCodecHandler::predicate, FailSoftMapCodecHandler::new);
        }
    }
}
