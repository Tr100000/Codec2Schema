package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.OptionalFieldCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OptionalFieldCodec.class)
public interface OptionalFieldCodecAccessor<A> {
    @Accessor
    String getName();

    @Accessor
    Codec<A> getElementCodec();
}
