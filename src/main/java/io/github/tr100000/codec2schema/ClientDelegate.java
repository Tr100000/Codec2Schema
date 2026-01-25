package io.github.tr100000.codec2schema;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public abstract class ClientDelegate {
    public static final ClientDelegate INSTANCE = create();

    private static ClientDelegate create() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            try {
                Class<?> implClass = Class.forName("io.github.tr100000.codec2schema.ClientDelegateClientImpl");
                return (ClientDelegate)implClass.getConstructor().newInstance();
            }
            catch (Exception e) {
                throw new IllegalStateException("Failed to create a ClientDelegate!", e);
            }
        }
        else {
            return new ClientDelegateCommonImpl();
        }
    }

    public abstract void registerHandlers();
    public abstract void generateSchemas();
}
