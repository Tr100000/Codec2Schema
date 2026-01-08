package io.github.tr100000.codec2schema.mixin;

import io.github.tr100000.codec2schema.Codec2Schema;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public abstract class BuiltInRegistriesMixin {
    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void bootstrap(CallbackInfo ci) {
        Codec2Schema.generateSchemas();
    }
}
