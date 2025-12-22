package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecWithValues;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
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
    @Inject(method = "referenceHolderWithLifecycle", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("unchecked")
    private <A> void referenceHolderWithLifecycle(CallbackInfoReturnable<Codec<Holder.Reference<A>>> cir) {
        Codec<Holder.Reference<A>> capturedReturnValue = cir.getReturnValue();
        Registry<A> thisRegistry = (Registry<A>)this;
        cir.setReturnValue(new CodecWithValues<>() {
            @Override
            public List<Holder.Reference<A>> possibleValues() {
                return thisRegistry.stream()
                        .map(thisRegistry::getKey)
                        .map(thisRegistry::get)
                        .map(Optional::orElseThrow)
                        .toList();
            }

            @Override
            public List<String> possibleStringValues() {
                return thisRegistry.stream()
                        .map(thisRegistry::getKey)
                        .map(Identifier::toString)
                        .toList();
            }

            @Override
            public Codec<Holder.Reference<A>> original() {
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
