package io.github.tr100000.codec2schema.compat.fabric;

import com.google.gson.JsonObject;
import io.github.tr100000.codec2schema.ModUtils;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.MapCodecHandlerRegistry;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class FabricCompatClient implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
        if (ModUtils.hasFabricModelLoadingApi()) {
            MapCodecHandlerRegistry.register(KeyExistsCodecHandler::predicate, KeyExistsCodecHandler::new);
        }
    }

    @Override
    public boolean shouldRun() {
        return ModUtils.hasFabricApi();
    }

    @Override
    public void writeExtraInfo(JsonObject pluginInfoJson) {
        if (!ModUtils.hasFabricApi()) {
            pluginInfoJson.addProperty("disabledReason", "Fabric API is not loaded");
        }
    }

    @Override
    public @Nullable Identifier getId() {
        return ModUtils.id("client/fabric_compat");
    }
}
