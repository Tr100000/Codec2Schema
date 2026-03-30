package io.github.tr100000.codec2schema;

public class ClientDelegateCommonImpl extends ClientDelegate {
    @Override
    public void registerHandlers() {
        Codec2Schema.registerHandlers(PluginSide.MAIN);
    }

    @Override
    public void generateSchemas() {
        Codec2Schema.generateSchemas(PluginSide.MAIN);
    }
}
