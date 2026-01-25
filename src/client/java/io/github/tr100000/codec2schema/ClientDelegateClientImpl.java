package io.github.tr100000.codec2schema;

import java.util.List;

public class ClientDelegateClientImpl extends ClientDelegate {
    private static final List<String> ENTRYPOINT_KEYS = List.of("codec2schema:main", "codec2schema:client");

    @Override
    public void registerHandlers() {
        Codec2Schema.registerHandlers(ENTRYPOINT_KEYS);
    }

    @Override
    public void generateSchemas() {
        Codec2Schema.generateSchemas(ENTRYPOINT_KEYS);
    }
}
