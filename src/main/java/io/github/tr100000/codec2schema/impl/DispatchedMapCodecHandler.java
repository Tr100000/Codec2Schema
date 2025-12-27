package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.DispatchedMapCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.Utils;
import io.github.tr100000.codec2schema.api.ValueStringPair;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DispatchedMapCodecHandler<K, V> implements CodecHandler<DispatchedMapCodec<K, V>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof DispatchedMapCodec<?,?>;
    }

    public static DispatchedMapCodecHandler<?, ?> createHandler() {
        return new DispatchedMapCodecHandler<>();
    }

    @Override
    public JsonObject toSchema(DispatchedMapCodec<K, V> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        Optional<List<ValueStringPair<K>>> possibleKeys = Utils.getPossibleValues(codec.keyCodec());
        if (possibleKeys.isPresent()) {
            return toSchema(possibleKeys.get().stream()
                    .map(pair -> pair.mapValue(codec.valueCodecFunction()::apply)), context);
        }
        else {
            JsonObject json = new JsonObject();
            json.addProperty("type", "object");
            return json;
        }
    }

    public static <T extends Codec<?>> JsonObject toSchema(Stream<ValueStringPair<T>> pairs, SchemaContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");

        JsonObject properties = new JsonObject();
        pairs.forEachOrdered(key -> properties.add(key.str(), context.requestDefinition(key.value())));
        json.add("properties", properties);

        return json;
    }
}
