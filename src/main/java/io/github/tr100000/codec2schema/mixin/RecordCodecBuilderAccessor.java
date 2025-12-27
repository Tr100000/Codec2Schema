package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecordCodecBuilder.class)
public interface RecordCodecBuilderAccessor<F> {
    @Accessor
    MapDecoder<F> getDecoder();
}
