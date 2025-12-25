package io.github.tr100000.codec2schema.mixin.record;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K1;
import io.github.tr100000.codec2schema.impl.map.MysteryClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Products.P7.class)
public abstract class ProductsP7Mixin<F extends K1, T1, T2, T3, T4, T5, T6, T7> {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, CallbackInfo ci) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7);
    }
}
