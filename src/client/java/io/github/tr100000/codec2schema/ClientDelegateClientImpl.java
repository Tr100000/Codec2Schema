package io.github.tr100000.codec2schema;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ClientDelegateClientImpl extends ClientDelegate {
    @Override
    public void registerHandlers() {
        Codec2Schema.registerHandlers(PluginSide.COMMON, PluginSide.CLIENT);
    }

    @Override
    public void generateSchemas() {
        Codec2Schema.generateSchemas(PluginSide.COMMON, PluginSide.CLIENT);
    }
}
