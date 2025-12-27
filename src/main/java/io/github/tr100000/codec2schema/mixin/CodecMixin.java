package io.github.tr100000.codec2schema.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;
import io.github.tr100000.codec2schema.api.codec.WrappedCodec;
import io.github.tr100000.codec2schema.impl.map.WrappedFieldMapCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Mixin(Codec.class)
public interface CodecMixin<A> {
    @WrapOperation(method = "xmap", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;of(Lcom/mojang/serialization/Encoder;Lcom/mojang/serialization/Decoder;Ljava/lang/String;)Lcom/mojang/serialization/Codec;"))
    private <S> Codec<S> xmap(Encoder<S> encoder, Decoder<S> decoder, String name, Operation<Codec<A>> original, @Local(argsOnly = true, ordinal = 0) Function<? super A, ? extends S> toFunction) {
        return wrapped(encoder, decoder, name, value -> DataResult.success(toFunction.apply(value)));
    }

    @WrapOperation(method = "comapFlatMap", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;of(Lcom/mojang/serialization/Encoder;Lcom/mojang/serialization/Decoder;Ljava/lang/String;)Lcom/mojang/serialization/Codec;"))
    private <S> Codec<S> comapFlatMap(Encoder<S> encoder, Decoder<S> decoder, String name, Operation<Codec<A>> original, @Local(argsOnly = true, ordinal = 0) Function<? super A, ? extends DataResult<? extends S>> toFunction) {
        return wrapped(encoder, decoder, name, toFunction);
    }

    @WrapOperation(method = "flatComapMap", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;of(Lcom/mojang/serialization/Encoder;Lcom/mojang/serialization/Decoder;Ljava/lang/String;)Lcom/mojang/serialization/Codec;"))
    private <S> Codec<S> flatComapMap(Encoder<S> encoder, Decoder<S> decoder, String name, Operation<Codec<A>> original, @Local(argsOnly = true, ordinal = 0) Function<? super A, ? extends S> toFunction) {
        return wrapped(encoder, decoder, name, value -> DataResult.success(toFunction.apply(value)));
    }

    @WrapOperation(method = "flatXmap", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;of(Lcom/mojang/serialization/Encoder;Lcom/mojang/serialization/Decoder;Ljava/lang/String;)Lcom/mojang/serialization/Codec;"))
    private <S> Codec<S> flatXmap(Encoder<S> encoder, Decoder<S> decoder, String name, Operation<Codec<A>> original, @Local(argsOnly = true, ordinal = 0) Function<? super A, ? extends DataResult<? extends S>> toFunction) {
        return wrapped(encoder, decoder, name, toFunction);
    }

    @Unique
    @SuppressWarnings("unchecked")
    private <S> Codec<S> wrapped(final Encoder<S> encoder, final Decoder<S> decoder, final String name, final Function<? super A, ? extends DataResult<? extends S>> toFunction) {
        Codec<A> thisCodec = (Codec<A>)this;
        Codec<S> fakeThisCodec = (Codec<S>)this;
        return switch (thisCodec) {
            case CodecWithValuePairs<A> codecWithValuePairs -> new CodecWithValuePairs<>() {
                @Override
                public List<ValueStringPair<S>> possibleValues() {
                    return codecWithValuePairs.possibleValues().stream()
                            .map(pair -> pair.mapValue(toFunction))
                            .filter(pair -> pair.value().hasResultOrPartial())
                            .map(pair -> pair.mapValue(DataResult::getPartialOrThrow))
                            .map(pair -> (ValueStringPair<S>)pair)
                            .toList();
                }

                @Override
                public Codec<S> original() {
                    return fakeThisCodec;
                }

                @Override
                public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
                    return decoder.decode(ops, input);
                }

                @Override
                public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
                    return encoder.encode(input, ops, prefix);
                }

                @Override
                public String toString() {
                    return name;
                }
            };
            default -> new WrappedCodec<>() {
                @Override
                public Codec<S> original() {
                    return fakeThisCodec;
                }

                @Override
                public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
                    return decoder.decode(ops, input);
                }

                @Override
                public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
                    return encoder.encode(input, ops, prefix);
                }

                @Override
                public String toString() {
                    return name;
                }
            };
        };
    }

    @WrapOperation(method = "fieldOf(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/MapCodec;of(Lcom/mojang/serialization/MapEncoder;Lcom/mojang/serialization/MapDecoder;Ljava/util/function/Supplier;)Lcom/mojang/serialization/MapCodec;"))
    @SuppressWarnings("unchecked")
    private MapCodec<A> fieldOf(MapEncoder<A> encoder, MapDecoder<A> decoder, Supplier<String> name, Operation<MapCodec<A>> original, @Local(argsOnly = true, ordinal = 0) final String fieldName) {
        Codec<A> thisCodec = (Codec<A>)this;
        return new WrappedFieldMapCodec<>() {
            @Override
            public Codec<A> original() {
                return thisCodec;
            }

            @Override
            public String fieldName() {
                return fieldName;
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return Stream.concat(encoder.keys(ops), decoder.keys(ops));
            }

            @Override
            public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return decoder.decode(ops, input);
            }

            @Override
            public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                return encoder.encode(input, ops, prefix);
            }

            @Override
            public String toString() {
                return name.get();
            }
        };
    }

    @Inject(method = "withLifecycle(Lcom/mojang/serialization/Lifecycle;)Lcom/mojang/serialization/Codec;", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private void withLifecycle(Lifecycle lifecycle, CallbackInfoReturnable<Codec<A>> cir) {
        Codec<A> capturedReturnValue = cir.getReturnValue();
        Codec<A> thisCodec = (Codec<A>)this;
        cir.setReturnValue(new WrappedCodec<>() {
            @Override
            public Codec<A> original() {
                return thisCodec;
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

    @Inject(method = "mapResult", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private void mapResult(Codec.ResultFunction<A> function, CallbackInfoReturnable<Codec<A>> cir) {
        Codec<A> capturedReturnValue = cir.getReturnValue();
        Codec<A> thisCodec = (Codec<A>)this;
        cir.setReturnValue(new WrappedCodec<>() {
            @Override
            public Codec<A> original() {
                return thisCodec;
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

    @Inject(method = "promotePartial(Ljava/util/function/Consumer;)Lcom/mojang/serialization/Codec;", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private void promotePartial(Consumer<String> onError, CallbackInfoReturnable<Codec<A>> cir) {
        Codec<A> capturedReturnValue = cir.getReturnValue();
        Codec<A> thisCodec = (Codec<A>)this;
        cir.setReturnValue(new WrappedCodec<>() {
            @Override
            public Codec<A> original() {
                return thisCodec;
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
