package io.github.tr100000.codec2schema.impl.specific;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.world.item.component.TypedEntityData;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface WrappedTypedEntityDataCodec<IdType> extends Codec<TypedEntityData<IdType>> {
    Codec<TypedEntityData<IdType>> original();

    @Override
    default <T> DataResult<Pair<TypedEntityData<IdType>, T>> decode(DynamicOps<T> ops, T input) {
        return original().decode(ops, input);
    }

    @Override
    default <T> DataResult<T> encode(TypedEntityData<IdType> input, DynamicOps<T> ops, T prefix) {
        return original().encode(input, ops, prefix);
    }
}
