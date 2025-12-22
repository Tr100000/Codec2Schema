package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyDispatchCodec.class)
public interface KeyDispatchCodecAccessor {
    @Accessor
    MapCodec<?> getKeyCodec();
}
