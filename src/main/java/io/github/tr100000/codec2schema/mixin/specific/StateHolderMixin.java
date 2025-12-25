package io.github.tr100000.codec2schema.mixin.specific;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.impl.specific.WrappedStateHolderCodec;
import net.minecraft.world.level.block.state.StateHolder;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(StateHolder.class)
@NullMarked
public abstract class StateHolderMixin {
    @Inject(method = "codec", at = @At("RETURN"), cancellable = true)
    private static <O, S extends StateHolder<O, S>> void codec(Codec<O> codec, Function<O, S> function, CallbackInfoReturnable<Codec<S>> cir) {
        Codec<S> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedStateHolderCodec<>() {
            @Override
            public Codec<S> original() {
                return capturedReturnValue;
            }

            @Override
            public Codec<?> dispatchedCodec() {
                return codec;
            }

            @Override
            @SuppressWarnings("unchecked")
            public Function<Object, S> toStateHolderFunction() {
                return (Function<Object, S>)function;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }
}
