package io.github.tr100000.codec2schema.mixin.record;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K1;
import io.github.tr100000.codec2schema.impl.map.MysteryClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Products.P3.class)
public abstract class ProductsP3Mixin<F extends K1, T1, T2, T3> {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, CallbackInfo ci) {
        MysteryClass.setSomething(t1, t2, t3);
    }
}
