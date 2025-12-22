package io.github.tr100000.codec2schema.mixin.specific;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.impl.specific.WrappedTypedEntityDataCodec;
import net.minecraft.world.item.component.TypedEntityData;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TypedEntityData.class)
@NullMarked
public class TypedEntityDataMixin {
    @Inject(method = "codec", at = @At("RETURN"), cancellable = true)
    private static <T> void codec(Codec<T> codec, CallbackInfoReturnable<Codec<TypedEntityData<T>>> cir) {
        Codec<TypedEntityData<T>> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedTypedEntityDataCodec<>() {
            @Override
            public Codec<TypedEntityData<T>> original() {
                return capturedReturnValue;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }
}
