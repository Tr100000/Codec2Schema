package io.github.tr100000.codec2schema.compat.trutils;

import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.CodecValueLister;
import net.fabricmc.loader.api.FabricLoader;

public class TrUtilsCompat implements Codec2SchemaPlugin {
    private boolean trutilsIsLoaded() {
        return FabricLoader.getInstance().isModLoaded("trutils");
    }

    @Override
    public void registerHandlers() {
        if (trutilsIsLoaded()) {
            CodecValueLister.LISTERS.add(new CodecFromMapLister());
        }
    }
}
