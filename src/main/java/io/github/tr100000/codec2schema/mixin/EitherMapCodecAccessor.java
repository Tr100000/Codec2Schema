package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.EitherMapCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EitherMapCodec.class)
public interface EitherMapCodecAccessor<F, S> {
    @Accessor
    MapCodec<F> getFirst();

    @Accessor
    MapCodec<S> getSecond();
}
