package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.api.WrappedCodec;
import io.github.tr100000.codec2schema.api.WrappedMapCodec;
import io.github.tr100000.codec2schema.impl.map.WrappedDispatchOptionalValueMapCodec;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

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

    @Inject(method = "dispatchOptionalValue", at = @At("RETURN"), cancellable = true)
    private static <K, V> void dispatchOptionalValue(String string, String string2, Codec<K> codec, Function<? super V, ? extends K> function, Function<? super K, ? extends Codec<? extends V>> function2, CallbackInfoReturnable<MapCodec<V>> cir) {
        MapCodec<V> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedDispatchOptionalValueMapCodec<K, V>() {
            @Override
            public MapCodec<V> original() {
                return capturedReturnValue;
            }

            @Override
            public String typeKey() {
                return string;
            }

            @Override
            public String dispatchKey() {
                return string2;
            }

            @Override
            public Codec<K> keyCodec() {
                return codec;
            }

            @Override
            public Function<? super K, ? extends Codec<? extends V>> valueCodecFunction() {
                return function2;
            }
        });
    }

    @Inject(method = "catchDecoderException", at = @At("RETURN"), cancellable = true)
    private static <A> void catchDecoderException(Codec<A> codec, CallbackInfoReturnable<Codec<A>> cir) {
        Codec<A> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedCodec<A>() {
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

    @Inject(method = "retrieveContext", at = @At("RETURN"), cancellable = true)
    private static <E> void retrieveContext(Function<DynamicOps<?>, DataResult<E>> function, CallbackInfoReturnable<MapCodec<E>> cir) {
        MapCodec<E> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedMapCodec<>() {
            @Override
            public MapCodec<?> original() {
                return capturedReturnValue;
            }

            @Override
            public boolean isUnitCodec() {
                return true;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }
}
