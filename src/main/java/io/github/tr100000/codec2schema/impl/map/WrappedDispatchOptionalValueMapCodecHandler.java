package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.Codec2Schema;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.Utils;
import io.github.tr100000.codec2schema.api.ValueStringPair;

import java.util.List;
import java.util.Optional;

public class WrappedDispatchOptionalValueMapCodecHandler<K, V> implements MapCodecHandler<WrappedDispatchOptionalValueMapCodec<K, V>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof WrappedDispatchOptionalValueMapCodec<?,?>;
    }

    public static WrappedDispatchOptionalValueMapCodecHandler<?, ?> create() {
        return new WrappedDispatchOptionalValueMapCodecHandler<>();
    }

    public static <K, V> JsonObject createSchema(WrappedDispatchOptionalValueMapCodec<K, V> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return new WrappedDispatchOptionalValueMapCodecHandler<K, V>().toSchema(codec, context, definitionContext);
    }

    @Override
    public void field(JsonObject json, JsonObject properties, JsonArray required, WrappedDispatchOptionalValueMapCodec<K, V> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        String typeKey = codec.typeKey();
        required.add(typeKey);
        String dispatchKey = codec.dispatchKey();
        if (context.debugMode) Codec2Schema.LOGGER.info("Starting dispatch: {} -> {}", typeKey, dispatchKey);

        Codec<K> keyCodec = codec.keyCodec();
        Optional<List<ValueStringPair<K>>> possibleValues = Utils.getPossibleValues(keyCodec);
        if (possibleValues.isPresent()) {
            JsonArray enumArray = new JsonArray();
            JsonArray allOfArray = JsonUtils.getOrCreateArray(json, "allOf");

            possibleValues.get().forEach(value -> {
                if (context.debugMode) Codec2Schema.LOGGER.info(value.str());

                JsonObject schema = new JsonObject();
                JsonObject schemaProperties = new JsonObject();
                schemaProperties.add(dispatchKey, context.requestDefinition(codec.valueCodecFunction().apply(value.value())));
                schema.add("properties", schemaProperties);

                enumArray.add(value.str());
                allOfArray.add(JsonUtils.schemaIfPropertyEquals(typeKey, value.str(), schema));
            });

            JsonObject enumProperty = new JsonObject();
            enumProperty.add("enum", enumArray);
            properties.add(typeKey, enumProperty);
        }
        else {
            properties.add(typeKey, new JsonObject());
        }

        if (context.debugMode) Codec2Schema.LOGGER.info("Finished dispatch: {} -> {}", typeKey, dispatchKey);
    }
}
