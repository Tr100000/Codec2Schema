package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecWithValues;
import io.github.tr100000.codec2schema.api.StringValuePair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(Registry.class)
@NullMarked
public interface RegistryMixin {
    @Inject(method = "byNameCodec", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private <T> void byNameCodec(CallbackInfoReturnable<Codec<T>> cir) {
        Codec<T> capturedReturnValue = cir.getReturnValue();
        Registry<T> thisRegistry = (Registry<T>)this;
        cir.setReturnValue(new CodecWithValues<>() {
            @Override
            public List<StringValuePair<T>> possibleValues() {
                return thisRegistry.stream()
                        .map(registered -> new StringValuePair<>(registered, thisRegistry.getKey(registered).toString()))
                        .toList();
            }

            @Override
            public Codec<T> original() {
                return capturedReturnValue;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }

            @Override
            public Optional<String> getName() {
                return Optional.of("registry:" + thisRegistry.key().identifier());
            }
        });
    }

    @Inject(method = "holderByNameCodec", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private <T> void referenceHolderWithLifecycle(CallbackInfoReturnable<Codec<Holder.Reference<T>>> cir) {
        Codec<Holder.Reference<T>> capturedReturnValue = cir.getReturnValue();
        Registry<T> thisRegistry = (Registry<T>)this;
        cir.setReturnValue(new CodecWithValues<>() {
            @Override
            public List<StringValuePair<Holder.Reference<T>>> possibleValues() {
                return thisRegistry.stream()
                        .map(thisRegistry::getKey)
                        .map(thisRegistry::get)
                        .map(Optional::orElseThrow)
                        .map(reference -> new StringValuePair<>(reference, reference.key().identifier().toString()))
                        .toList();
            }

            @Override
            public Codec<Holder.Reference<T>> original() {
                return capturedReturnValue;
            }

            @Override
            public String toString() {
                return capturedReturnValue.toString();
            }

            @Override
            public Optional<String> getName() {
                return Optional.of("registry:" + thisRegistry.key().identifier());
            }
        });
    }
}
