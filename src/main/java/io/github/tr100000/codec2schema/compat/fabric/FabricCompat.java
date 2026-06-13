package io.github.tr100000.codec2schema.compat.fabric;

import io.github.tr100000.codec2schema.ModUtils;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.CodecHandlerRegistry;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class FabricCompat implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
        if (ModUtils.hasFabricDimensionsApi()) {
            CodecHandlerRegistry.register(FailSoftMapCodecHandler::predicate, FailSoftMapCodecHandler::new);
        }
    }

    @Override
    public boolean shouldRun() {
        return ModUtils.hasFabricDimensionsApi();
    }

    @Override
    public @Nullable Identifier getId() {
        return ModUtils.id("common/fabric_compat");
    }
}
