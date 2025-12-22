package io.github.tr100000.codec2schema.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedEnumCodec;
import io.github.tr100000.codec2schema.impl.wrapped.WrappedStringRepresentableCodec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(StringRepresentable.class)
@NullMarked
public interface StringRepresentableMixin {
    @Inject(method = "fromEnumWithMapping", at = @At("RETURN"), cancellable = true)
    private static <E extends Enum<E> & StringRepresentable> void fromEnumWithMapping(Supplier<E[]> supplier, Function<String, String> function, CallbackInfoReturnable<StringRepresentable.EnumCodec<E>> cir, @Local E[] enums, @Local(ordinal = 1) Function<String, E> function2) {
        StringRepresentable.EnumCodec<E> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedEnumCodec<>(enums, function2) {
            @Override
            public Codec<E> getOriginal() {
                return capturedReturnValue;
            }

            @Override
            public String[] getEnumValues() {
                return toStringArray(enums);
            }
        });
    }

    @Inject(method = "fromValues", at = @At("RETURN"), cancellable = true)
    private static <T extends StringRepresentable> void fromValues(Supplier<T[]> supplier, CallbackInfoReturnable<Codec<T>> cir, @Local T[] enumValues) {
        Codec<T> capturedReturnValue = cir.getReturnValue();
        cir.setReturnValue(new WrappedStringRepresentableCodec<>() {
            @Override
            public Codec<T> getOriginal() {
                return capturedReturnValue;
            }

            @Override
            public String[] getEnumValues() {
                return toStringArray(enumValues);
            }
        });
    }

    @Unique
    private static String[] toStringArray(StringRepresentable[] values) {
        String[] enumValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            enumValues[i] = values[i].getSerializedName();
        }
        return enumValues;
    }
}
