package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.Codec2Schema;
import io.github.tr100000.codec2schema.api.NumberBound;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;
import io.github.tr100000.codec2schema.api.codec.WrappedCodec;
import io.github.tr100000.codec2schema.api.codec.WrappedMapCodec;
import io.github.tr100000.codec2schema.impl.map.WrappedDispatchOptionalValueMapCodec;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedRangedNumberCodec;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(ExtraCodecs.class)
public abstract class ExtraCodecsMixin {
    @Inject(method = "intRangeWithMessage", at = @At("RETURN"), cancellable = true)
    private static void intRangeWithMessage(int minInclusive, int maxInclusive, Function<Integer, String> error, CallbackInfoReturnable<Codec<Integer>> cir) {
        Codec<Integer> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedRangedNumberCodec<>() {
            @Override
            public Codec<Integer> original() {
                return capturedReturnValue;
            }

            @Override
            public Optional<NumberBound<Integer>> min() {
                return minInclusive > Integer.MIN_VALUE ? Optional.of(new NumberBound<>(minInclusive, false)) : Optional.empty();
            }

            @Override
            public Optional<NumberBound<Integer>> max() {
                return maxInclusive < Integer.MAX_VALUE ? Optional.of(new NumberBound<>(maxInclusive, false)) : Optional.empty();
            }
        });
    }

    @Inject(method = "longRangeWithMessage", at = @At("RETURN"), cancellable = true)
    private static void longRangeWithMessage(long minInclusive, long maxInclusive, Function<Long, String> error, CallbackInfoReturnable<Codec<Long>> cir) {
        Codec<Long> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedRangedNumberCodec<>() {
            @Override
            public Codec<Long> original() {
                return capturedReturnValue;
            }

            @Override
            public Optional<NumberBound<Long>> min() {
                return minInclusive > Long.MIN_VALUE ? Optional.of(new NumberBound<>(minInclusive, false)) : Optional.empty();
            }

            @Override
            public Optional<NumberBound<Long>> max() {
                return maxInclusive < Long.MAX_VALUE ? Optional.of(new NumberBound<>(maxInclusive, false)) : Optional.empty();
            }
        });
    }

    @Inject(method = "floatRangeMinInclusiveWithMessage", at = @At("RETURN"), cancellable = true)
    private static void floatRangeMinInclusiveWithMessage(float minInclusive, float maxInclusive, Function<Float, String> error, CallbackInfoReturnable<Codec<Float>> cir) {
        Codec<Float> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedRangedNumberCodec<>() {
            @Override
            public Codec<Float> original() {
                return capturedReturnValue;
            }

            @Override
            public boolean isInteger() {
                return false;
            }

            @Override
            public Optional<NumberBound<Float>> min() {
                return minInclusive > Float.MIN_VALUE ? Optional.of(new NumberBound<>(minInclusive, false)) : Optional.empty();
            }

            @Override
            public Optional<NumberBound<Float>> max() {
                return maxInclusive < Float.MAX_VALUE ? Optional.of(new NumberBound<>(maxInclusive, false)) : Optional.empty();
            }
        });
    }

    @Inject(method = "floatRangeMinExclusiveWithMessage", at = @At("RETURN"), cancellable = true)
    private static void floatRangeMinExclusiveWithMessage(float minExclusive, float maxInclusive, Function<Float, String> error, CallbackInfoReturnable<Codec<Float>> cir) {
        Codec<Float> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedRangedNumberCodec<>() {
            @Override
            public Codec<Float> original() {
                return capturedReturnValue;
            }

            @Override
            public boolean isInteger() {
                return false;
            }

            @Override
            public Optional<NumberBound<Float>> min() {
                return minExclusive > Float.MIN_VALUE ? Optional.of(new NumberBound<>(minExclusive, true)) : Optional.empty();
            }

            @Override
            public Optional<NumberBound<Float>> max() {
                return maxInclusive < Float.MAX_VALUE ? Optional.of(new NumberBound<>(maxInclusive, false)) : Optional.empty();
            }
        });
    }

    @Inject(method = "orCompressed(Lcom/mojang/serialization/Codec;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;", at = @At("RETURN"), cancellable = true)
    private static <E> void orCompressed(Codec<E> normal, Codec<E> compressed, CallbackInfoReturnable<Codec<E>> cir) {
        Codec<E> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedCodec<>() {
            @Override
            public Codec<E> original() {
                return normal;
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
    private static <E> void orCompressedMap(MapCodec<E> normal, MapCodec<E> compressed, CallbackInfoReturnable<MapCodec<E>> cir) {
        MapCodec<E> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedMapCodec<>() {
            @Override
            public MapCodec<E> original() {
                return normal;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }

    @Inject(method = "dispatchOptionalValue", at = @At("RETURN"), cancellable = true)
    private static <K, V> void dispatchOptionalValue(String typeKey, String valueKey, Codec<K> typeCodec, Function<? super V, ? extends K> typeGetter, Function<? super K, ? extends Codec<? extends V>> valueCodec, CallbackInfoReturnable<MapCodec<V>> cir) {
        MapCodec<V> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedDispatchOptionalValueMapCodec<K, V>() {
            @Override
            public MapCodec<V> original() {
                return capturedReturnValue;
            }

            @Override
            public String typeKey() {
                return typeKey;
            }

            @Override
            public String dispatchKey() {
                return valueKey;
            }

            @Override
            public Codec<K> keyCodec() {
                return typeCodec;
            }

            @Override
            public Function<? super K, ? extends Codec<? extends V>> valueCodecFunction() {
                return valueCodec;
            }
        });
    }

    @Inject(method = "catchDecoderException", at = @At("RETURN"), cancellable = true)
    private static <A> void catchDecoderException(Codec<A> codec, CallbackInfoReturnable<Codec<A>> cir) {
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

    @Inject(method = "retrieveContext", at = @At("RETURN"), cancellable = true)
    private static <E> void retrieveContext(Function<DynamicOps<?>, DataResult<E>> getter, CallbackInfoReturnable<MapCodec<E>> cir) {
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

    @Inject(method = "idResolverCodec(Lcom/mojang/serialization/Codec;Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;", at = @At("RETURN"), cancellable = true)
    private static <I, E> void idResolverCodec(Codec<I> value, Function<I, E> fromId, Function<E, I> toId, CallbackInfoReturnable<Codec<E>> cir) {
        Codec<E> capturedReturnValue = cir.getReturnValue();
        if (value instanceof CodecWithValuePairs<I> codecWithValuePairs) {
            cir.setReturnValue(new CodecWithValuePairs<>() {
                @Override
                public List<ValueStringPair<E>> possibleValues() {
                    return codecWithValuePairs.possibleValues().stream()
                            .map(pair -> pair.mapValue(fromId))
                            .toList();
                }

                @Override
                public Codec<E> original() {
                    return capturedReturnValue;
                }

                @Override
                public String toString() {
                    return capturedReturnValue.toString();
                }

                @Override
                public Codec<?> fallbackCodec() {
                    return value;
                }
            });
        }
        else {
            Codec2Schema.LOGGER.warn("uh oh");
        }
    }
}
