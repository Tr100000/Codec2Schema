package io.github.tr100000.codec2schema.mixin.record;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K1;
import io.github.tr100000.codec2schema.impl.map.MysteryClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Products.P4.class)
public abstract class ProductsP4Mixin<F extends K1, T1, T2, T3, T4> {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(final App<F, T1> t1, final App<F, T2> t2, final App<F, T3> t3, final App<F, T4> t4, CallbackInfo ci) {
        MysteryClass.setSomething(t1, t2, t3, t4);
    }
}
