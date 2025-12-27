package io.github.tr100000.codec2schema.mixin.compat.fabric;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.fabricmc.fabric.impl.client.model.loading.CustomUnbakedBlockStateModelRegistry;
import net.minecraft.client.renderer.block.model.SingleVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CustomUnbakedBlockStateModelRegistry.class)
public interface CustomUnbakedBlockStateModelRegistryAccessor {
    @Accessor(value = "TYPE_KEY")
    static String getTypeKey() {
        throw new AssertionError();
    }

    @Accessor(value = "CUSTOM_MODEL_MAP_CODEC")
    static MapCodec<CustomUnbakedBlockStateModel> getCustomModelMapCodec() {
        throw new AssertionError();
    }

    @Accessor(value = "SIMPLE_MODEL_MAP_CODEC")
    static MapCodec<SingleVariant.Unbaked> getSimpleModelMapCodec() {
        throw new AssertionError();
    }

    @Accessor(value = "VARIANT_MAP_CODEC")
    static MapCodec<Either<CustomUnbakedBlockStateModel, SingleVariant.Unbaked>> getVariantMapCodec() {
        throw new AssertionError();
    }
}
