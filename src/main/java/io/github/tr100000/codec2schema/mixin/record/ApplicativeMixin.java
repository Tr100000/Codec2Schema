package io.github.tr100000.codec2schema.mixin.record;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import io.github.tr100000.codec2schema.impl.map.MysteryClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;

@Mixin(Applicative.class)
public interface ApplicativeMixin<F extends K1> {
    @Inject(method = "apply2", at = @At("RETURN"))
    private <A, B, R> void apply2(BiFunction<A, B, R> func, App<F, A> a, App<F, B> b, CallbackInfoReturnable<App<F, R>> cir) {
        // This is used in exactly one place
        MysteryClass.setSomething(a, b);
    }
}
