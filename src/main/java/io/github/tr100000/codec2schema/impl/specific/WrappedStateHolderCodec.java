package io.github.tr100000.codec2schema.impl.specific;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.codec.WrappedCodec;
import net.minecraft.world.level.block.state.StateHolder;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public interface WrappedStateHolderCodec<A extends StateHolder<?, A>> extends WrappedCodec<A> {
    Codec<?> dispatchedCodec();
    Function<Object, A> toStateHolderFunction();
}
