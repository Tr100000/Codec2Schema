package io.github.tr100000.codec2schema.compat.trutils;

import io.github.tr100000.codec2schema.ModUtils;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.CodecValueLister;

public class TrUtilsCompat implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
        if (ModUtils.hasTrUtils()) {
            CodecValueLister.LISTERS.add(new CodecFromMapLister());
        }
    }
}
