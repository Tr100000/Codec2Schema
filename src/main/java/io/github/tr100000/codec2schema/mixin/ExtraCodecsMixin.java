package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.api.WrappedCodec;
import io.github.tr100000.codec2schema.api.WrappedMapCodec;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExtraCodecs.class)
public abstract class ExtraCodecsMixin {
    @Inject(method = "orCompressed(Lcom/mojang/serialization/Codec;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;", at = @At("RETURN"), cancellable = true)
    private static <E> void orCompressed(Codec<E> codec, Codec<E> codec2, CallbackInfoReturnable<Codec<E>> cir) {
        Codec<E> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedCodec<>() {
            @Override
            public Codec<E> original() {
                return codec;
            }

            @Override
            public Codec<E> getWrapped() {
                return capturedReturnValue;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }

    @Inject(method = "orCompressed(Lcom/mojang/serialization/MapCodec;Lcom/mojang/serialization/MapCodec;)Lcom/mojang/serialization/MapCodec;", at = @At("RETURN"), cancellable = true)
    private static <E> void orCompressedMap(MapCodec<E> mapCodec, MapCodec<E> mapCodec2, CallbackInfoReturnable<MapCodec<E>> cir) {
        MapCodec<E> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedMapCodec<>() {
            @Override
            public MapCodec<E> original() {
                return mapCodec;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }
}
