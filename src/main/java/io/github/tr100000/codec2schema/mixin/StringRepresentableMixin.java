package io.github.tr100000.codec2schema.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedEnumCodec;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedStringRepresentableCodec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(StringRepresentable.class)
@NullMarked
public interface StringRepresentableMixin {
    @Inject(method = "fromEnumWithMapping", at = @At("RETURN"), cancellable = true)
    private static <E extends Enum<E> & StringRepresentable> void fromEnumWithMapping(Supplier<E[]> values, Function<String, String> converter, CallbackInfoReturnable<StringRepresentable.EnumCodec<E>> cir, @Local(name = "valueArray") E[] valueArray, @Local(name = "lookupFunction") Function<String, E> lookupFunction) {
        StringRepresentable.EnumCodec<E> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedEnumCodec<>(valueArray, lookupFunction) {
            @Override
            public Codec<E> original() {
                return capturedReturnValue;
            }

            @Override
            public List<ValueStringPair<E>> possibleValues() {
                return toStringList(valueArray, value -> converter.apply(value.getSerializedName()));
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }

    @Inject(method = "fromValues", at = @At("RETURN"), cancellable = true)
    private static <T extends StringRepresentable> void fromValues(Supplier<T[]> values, CallbackInfoReturnable<Codec<T>> cir, @Local(name = "valueArray") T[] valueArray) {
        Codec<T> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedStringRepresentableCodec<>() {
            @Override
            public Codec<T> original() {
                return capturedReturnValue;
            }

            @Override
            public List<ValueStringPair<T>> possibleValues() {
                return toStringList(valueArray, StringRepresentable::getSerializedName);
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }
        });
    }

    @Unique
    private static <T extends StringRepresentable> List<ValueStringPair<T>> toStringList(T[] values, Function<T, String> function) {
        return Arrays.stream(values)
                .map(value -> new ValueStringPair<>(value, function.apply(value)))
                .toList();
    }
}
