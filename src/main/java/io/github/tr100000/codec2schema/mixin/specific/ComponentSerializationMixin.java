package io.github.tr100000.codec2schema.mixin.specific;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.impl.specific.WrappedRestrictedComponentCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComponentSerialization.class)
public class ComponentSerializationMixin {
    @Inject(method = "flatRestrictedCodec", at = @At("RETURN"), cancellable = true)
    private static void flatRestrictedCodec(int i, CallbackInfoReturnable<Codec<Component>> cir) {
        Codec<Component> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedRestrictedComponentCodec() {
            @Override
            public Codec<Component> wrapped() {
                return capturedReturnValue;
            }

            @Override
            public int maxSize() {
                return i;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }
}
