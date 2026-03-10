package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.CompoundListCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CompoundListCodec.class)
public interface CompoundListCodecAccessor<K, V> {
    @Accessor
    Codec<K> getKeyCodec();

    @Accessor
    Codec<V> getElementCodec();
}
