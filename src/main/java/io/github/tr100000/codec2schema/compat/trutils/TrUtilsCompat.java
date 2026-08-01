package io.github.tr100000.codec2schema.compat.trutils;

import com.google.gson.JsonObject;
import io.github.tr100000.codec2schema.ModUtils;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.CodecValueLister;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class TrUtilsCompat implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
        CodecValueLister.LISTERS.add(new CodecFromMapLister());
    }

    @Override
    public boolean shouldRun() {
        return ModUtils.hasTrUtils();
    }

    @Override
    public void writeExtraInfo(JsonObject pluginInfoJson) {
        if (!ModUtils.hasTrUtils()) {
            pluginInfoJson.addProperty("disabledReason", "TrUtils is not loaded");
        }
    }

    @Override
    public @Nullable Identifier getId() {
        return ModUtils.id("common/trutils_compat");
    }
}
