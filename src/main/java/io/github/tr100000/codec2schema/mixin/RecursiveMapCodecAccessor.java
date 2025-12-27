package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.MapCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(targets = "com/mojang/serialization/MapCodec$RecursiveMapCodec")
public interface RecursiveMapCodecAccessor<A> {
    @Accessor
    Supplier<MapCodec<A>> getWrapped();
}
