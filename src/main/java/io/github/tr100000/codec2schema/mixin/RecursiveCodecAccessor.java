package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(Codec.RecursiveCodec.class)
public interface RecursiveCodecAccessor<T> {
    @Accessor
    Supplier<Codec<T>> getWrapped();
}
