package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.WrappedCodec;
import net.minecraft.util.EncoderCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EncoderCache.class)
public abstract class EncoderCacheMixin {
    @Inject(method = "wrap", at = @At("RETURN"), cancellable = true)
    private <A> void wrap(Codec<A> codec, CallbackInfoReturnable<Codec<A>> cir) {
        Codec<A> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedCodec<>() {
            @Override
            public Codec<A> original() {
                return codec;
            }

            @Override
            public Codec<A> getWrapped() {
                return capturedReturnValue;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }
}
