package io.github.tr100000.codec2schema.mixin;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(KeyDispatchCodec.class)
public interface KeyDispatchCodecAccessor<K, V> {
    @Accessor
    MapCodec<K> getKeyCodec();

    @Accessor
    Function<? super K, ? extends DataResult<? extends MapDecoder<? extends V>>> getDecoder();
}
