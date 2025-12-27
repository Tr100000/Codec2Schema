package io.github.tr100000.codec2schema.mixin;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.api.codec.WrappedMapCodec;
import io.github.tr100000.codec2schema.api.codec.WrappedUnitCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(MapCodec.class)
public abstract class MapCodecMixin<A> {
    @Inject(method = "xmap", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private <S> void xmap(Function<? super A, ? extends S> to, Function<? super S, ? extends A> from, CallbackInfoReturnable<MapCodec<S>> cir) {
        MapCodec<S> capturedReturnValue = cir.getReturnValue();
        MapCodec<A> thisCodec = (MapCodec<A>)(Object)this;
        cir.setReturnValue(new WrappedMapCodec<>() {
            @Override
            public MapCodec<?> original() {
                return thisCodec;
            }

            @Override
            public MapCodec<S> getWrapped() {
                return capturedReturnValue;
            }
        });
    }

    @Inject(method = "flatXmap", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private <S> void flatXmap(Function<? super A, ? extends DataResult<? extends S>> to, Function<? super S, ? extends DataResult<? extends A>> from, CallbackInfoReturnable<MapCodec<S>> cir) {
        MapCodec<S> capturedReturnValue = cir.getReturnValue();
        MapCodec<A> thisCodec = (MapCodec<A>)(Object)this;
        cir.setReturnValue(new WrappedMapCodec<>() {
            @Override
            public MapCodec<?> original() {
                return thisCodec;
            }

            @Override
            public MapCodec<S> getWrapped() {
                return capturedReturnValue;
            }
        });
    }

    @Inject(method = "withLifecycle(Lcom/mojang/serialization/Lifecycle;)Lcom/mojang/serialization/MapCodec;", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private void withLifecycle(Lifecycle lifecycle, CallbackInfoReturnable<MapCodec<A>> cir) {
        MapCodec<A> capturedReturnValue = cir.getReturnValue();
        MapCodec<A> thisCodec = (MapCodec<A>)(Object)this;
        cir.setReturnValue(new WrappedMapCodec<>() {
            @Override
            public MapCodec<A> original() {
                return thisCodec;
            }

            @Override
            public MapCodec<A> getWrapped() {
                return capturedReturnValue;
            }
        });
    }

    @Inject(method = "mapResult", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private void mapResult(MapCodec.ResultFunction<A> function, CallbackInfoReturnable<MapCodec<A>> cir) {
        MapCodec<A> capturedReturnValue = cir.getReturnValue();
        MapCodec<A> thisCodec = (MapCodec<A>)(Object)this;
        cir.setReturnValue(new WrappedMapCodec<>() {
            @Override
            public MapCodec<A> original() {
                return thisCodec;
            }

            @Override
            public MapCodec<A> getWrapped() {
                return capturedReturnValue;
            }
        });
    }

    @Inject(method = "unit(Ljava/util/function/Supplier;)Lcom/mojang/serialization/MapCodec;", at = @At("RETURN"), cancellable = true)
    private static <A> void unit(Supplier<A> value, CallbackInfoReturnable<MapCodec<A>> cir) {
        MapCodec<A> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedMapCodec<>() {
            @Override
            public MapCodec<?> original() {
                return capturedReturnValue;
            }

            @Override
            public boolean isUnitCodec() {
                return true;
            }
        });
    }

    @Inject(method = "unitCodec(Ljava/lang/Object;)Lcom/mojang/serialization/Codec;", at = @At("RETURN"), cancellable = true)
    private static <A> void unitCodec(A value, CallbackInfoReturnable<Codec<A>> cir) {
        Codec<A> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedUnitCodec<>() {
            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
                return capturedReturnValue.decode(ops, input);
            }

            @Override
            public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
                return capturedReturnValue.encode(input, ops, prefix);
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }

    @Inject(method = "recursive", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private static <A> void recursive(String name, Function<Codec<A>, MapCodec<A>> wrapped, CallbackInfoReturnable<MapCodec<A>> cir) {
        MapCodec<A> capturedReturnValue = cir.getReturnValue();
        MapCodec<A> wrappedCodec = ((RecursiveMapCodecAccessor<A>)capturedReturnValue).getWrapped().get();
        cir.setReturnValue(new WrappedMapCodec<>() {
            @Override
            public MapCodec<?> original() {
                return wrappedCodec;
            }

            @Override
            public MapCodec<A> getWrapped() {
                return capturedReturnValue;
            }
        });
    }
}
