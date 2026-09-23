package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.Utils;
import io.github.tr100000.codec2schema.api.ValueStringPair;

import java.util.List;
import java.util.Optional;

public class WrappedSingleKeyDispatchCodecHandler<K, V> implements CodecHandler<WrappedSingleKeyDispatchCodec<K, V>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedSingleKeyDispatchCodec<?, ?>;
    }

    public static WrappedSingleKeyDispatchCodecHandler<?, ?> createHandler() {
        return new WrappedSingleKeyDispatchCodecHandler<>();
    }

    @Override
    public JsonObject toSchema(WrappedSingleKeyDispatchCodec<K, V> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        Optional<List<ValueStringPair<K>>> possibleValues = Utils.getPossibleValues(codec.keyCodec());

        JsonObject json = new JsonObject();
        json.addProperty("type", "object");

        if (possibleValues.isPresent()) {
            JsonArray oneOf = JsonUtils.getOrCreateArray(json, "oneOf");
            for (ValueStringPair<K> valuePair : possibleValues.get()) {
                JsonObject keyJson = new JsonObject();
                JsonUtils.getOrCreateArray(keyJson, "required").add(valuePair.str());
                JsonUtils.getOrCreateObject(keyJson, "properties")
                        .add(valuePair.str(), context.requestDefinition(codec.valueCodecGetter().apply(valuePair.value())));
                keyJson.addProperty("additionalProperties", false);

                oneOf.add(keyJson);
            }
        }

        return json;
    }
}
