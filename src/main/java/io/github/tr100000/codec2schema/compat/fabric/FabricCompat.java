package io.github.tr100000.codec2schema.compat.fabric;

import io.github.tr100000.codec2schema.api.CodecHandlerRegistrationEntrypoint;
import io.github.tr100000.codec2schema.api.MapCodecHandlerRegistry;

public class FabricCompat implements CodecHandlerRegistrationEntrypoint {
    @Override
    public void register() {
        MapCodecHandlerRegistry.register(KeyExistsCodecHandler::predicate, KeyExistsCodecHandler::new);
    }
}
