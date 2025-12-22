package io.github.tr100000.codec2schema.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.codec2schema.impl.map.MysteryClass;
import io.github.tr100000.codec2schema.impl.map.WrappedRecordCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RecordCodecBuilder.class)
public abstract class RecordCodecBuilderMixin {
    @Inject(method = "build", at = @At("RETURN"), cancellable = true)
    private static <O> void build(App<RecordCodecBuilder.Mu<O>, O> builderBox, CallbackInfoReturnable<MapCodec<O>> cir, @Local(name = "builder") RecordCodecBuilder<O, O> builder) {
        MapCodec<O> originalCodec = cir.getReturnValue();
        List<RecordCodecBuilder<?, ?>> fields = MysteryClass.something;
        MysteryClass.something = null;
        cir.setReturnValue(new WrappedRecordCodec<>() {
            @Override
            public List<RecordCodecBuilder<?, ?>> fields() {
                return fields;
            }

            @Override
            public MapCodec<O> original() {
                return originalCodec;
            }
        });
    }
}
