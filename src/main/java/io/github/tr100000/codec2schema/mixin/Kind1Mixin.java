package io.github.tr100000.codec2schema.mixin;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.Kind1;
import io.github.tr100000.codec2schema.impl.map.MysteryClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Let's pretend that this doesn't exist.
@Mixin(Kind1.class)
public interface Kind1Mixin<F extends K1> {
    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P1;",
            at = @At("HEAD")
    )
    private <T1> void group1(App<F, T1> t1, CallbackInfoReturnable<Products.P1<F, T1>> cir) {
        MysteryClass.setSomething(t1);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P2;",
            at = @At("HEAD")
    )
    private <T1, T2> void group2(App<F, T1> t1, App<F, T2> t2, CallbackInfoReturnable<Products.P2<F, T1, T2>> cir) {
        MysteryClass.setSomething(t1, t2);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P3;",
            at = @At("HEAD")
    )
    private <T1, T2, T3> void group3(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, CallbackInfoReturnable<Products.P3<F, T1, T2, T3>> cir) {
        MysteryClass.setSomething(t1, t2, t3);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P4;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4> void group4(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, CallbackInfoReturnable<Products.P4<F, T1, T2, T3, T4>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P5;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5> void group5(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, CallbackInfoReturnable<Products.P5<F, T1, T2, T3, T4, T5>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P6;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6> void group6(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, CallbackInfoReturnable<Products.P6<F, T1, T2, T3, T4, T5, T6>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P7;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7> void group7(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, CallbackInfoReturnable<Products.P7<F, T1, T2, T3, T4, T5, T6, T7>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P8;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7, T8> void group8(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, CallbackInfoReturnable<Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P9;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7, T8, T9> void group9(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, CallbackInfoReturnable<Products.P9<F, T1, T2, T3, T4, T5, T6, T7, T8, T9>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P10;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> void group10(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, CallbackInfoReturnable<Products.P10<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P11;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> void group11(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, CallbackInfoReturnable<Products.P11<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P12;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> void group12(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, CallbackInfoReturnable<Products.P12<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P13;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> void group13(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, CallbackInfoReturnable<Products.P13<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P14;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> void group14(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14, CallbackInfoReturnable<Products.P14<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P15;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> void group15(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14, App<F, T15> t15, CallbackInfoReturnable<Products.P15<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Inject(
            method = "group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P16;",
            at = @At("HEAD")
    )
    private <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> void group16(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14, App<F, T15> t15, App<F, T16> t16, CallbackInfoReturnable<Products.P16<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> cir) {
        MysteryClass.setSomething(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }
}
