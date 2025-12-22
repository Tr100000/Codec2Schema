package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.DispatchedMapCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.Utils;
import net.minecraft.core.Holder;

import java.util.List;
import java.util.Optional;

public class DispatchedMapCodecHandler<K, V> implements CodecHandler<DispatchedMapCodec<K, V>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof DispatchedMapCodec<?,?>;
    }

    public static DispatchedMapCodecHandler<?, ?> createHandler() {
        return new DispatchedMapCodecHandler<>();
    }

    @Override
    public JsonObject toSchema(DispatchedMapCodec<K, V> codec, SchemaContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");

        Optional<List<K>> possibleKeys = Utils.getPossibleValues(codec.keyCodec());
        if (possibleKeys.isPresent()) {
            JsonObject properties = new JsonObject();
            possibleKeys.get().forEach(key -> {
                String keyStr = getStringHopefully(key);
                Codec<? extends V> valueCodec = codec.valueCodecFunction().apply(key);
                properties.add(keyStr, context.requestDefinition(valueCodec));
            });
            json.add("properties", properties);
        }

        return json;
    }

    private String getStringHopefully(Object obj) {
        if (obj instanceof Holder<?> holder) return holder.getRegisteredName();
        return obj.toString();
    }
}
