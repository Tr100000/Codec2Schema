package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.HolderSetCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HolderSetCodec.class)
public interface HolderSetCodecAccessor {
    @Accessor
    Codec<Holder<?>> getElementCodec();
}
