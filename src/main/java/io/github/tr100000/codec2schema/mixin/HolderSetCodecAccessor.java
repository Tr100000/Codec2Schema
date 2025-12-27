package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.HolderSetCodec;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HolderSetCodec.class)
@NullMarked
public interface HolderSetCodecAccessor<E> {
    @Accessor
    Codec<Holder<E>> getElementCodec();
}
