package io.github.tr100000.codec2schema.mixin;

import com.google.common.collect.BiMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Function;

@Mixin(ExtraCodecs.LateBoundIdMapper.class)
public abstract class LateBoundIdMapperMixin<I, V> {
    @Shadow @Final
    private BiMap<I, V> idToValue;

    @WrapOperation(method = "codec", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ExtraCodecs;idResolverCodec(Lcom/mojang/serialization/Codec;Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"))
    private Codec<V> codec(Codec<I> value, Function<I, V> fromId, Function<V, I> toId, Operation<Codec<V>> original) {
        Codec<I> wrappedCodec = new CodecWithValuePairs<>() {
            @Override
            public List<ValueStringPair<I>> possibleValues() {
                return idToValue.keySet().stream()
                        .map(value -> new ValueStringPair<>(value, value.toString()))
                        .toList();
            }

            @Override
            public Codec<I> original() {
                return value;
            }

            @Override
            public Codec<?> fallbackCodec() {
                return value;
            }
        };
        return original.call(wrappedCodec, fromId, toId);
    }
}
