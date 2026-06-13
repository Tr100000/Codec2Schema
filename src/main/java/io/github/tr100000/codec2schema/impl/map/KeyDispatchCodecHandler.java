package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import io.github.tr100000.codec2schema.Codec2Schema;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.Utils;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.mixin.KeyDispatchCodecAccessor;

import java.util.List;
import java.util.Optional;

public class KeyDispatchCodecHandler<K, V> implements MapCodecHandler<KeyDispatchCodec<K, V>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof KeyDispatchCodec<?, ?>;
    }

    public static KeyDispatchCodecHandler<?, ?> create() {
        return new KeyDispatchCodecHandler<>();
    }

    public static <K, V> JsonObject createSchema(KeyDispatchCodec<K, V> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return new KeyDispatchCodecHandler<K, V>().toSchema(codec, context, definitionContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void field(JsonObject json, JsonObject properties, JsonArray required, KeyDispatchCodec<K, V> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        MapCodec<K> keyCodec = ((KeyDispatchCodecAccessor<K, V>)codec).getKeyCodec();
        String typeFieldName = Utils.getFieldNameForDispatch(MapCodecCodecHandler.actually(keyCodec), required::add);

        if (context.debugMode) Codec2Schema.LOGGER.info("Starting dispatch: {}", typeFieldName);

        Optional<List<ValueStringPair<K>>> possibleValues = Utils.getPossibleValues(keyCodec);
        if (possibleValues.isPresent()) {
            JsonArray enumArray = new JsonArray();
            JsonArray allOfArray = JsonUtils.getOrCreateArray(json, "allOf");

            possibleValues.get().forEach(value -> {
                if (context.debugMode) Codec2Schema.LOGGER.info(value.str());
                if (value.value() != null) {
                    enumArray.add(value.str());
                    DataResult<MapDecoder<V>> decoder = (DataResult<MapDecoder<V>>)((KeyDispatchCodecAccessor<K, V>)codec).getDecoder().apply(value.value());
                    if (decoder.hasResultOrPartial()) {
                        if (decoder.getPartialOrThrow() instanceof MapCodec<V> mapCodec) {
                            allOfArray.add(JsonUtils.schemaIfPropertyEquals(typeFieldName, value.str(), context.requestDefinition(mapCodec.codec())));
                        }
                        else {
                            throw new IllegalStateException(String.format("Map decoder was of type %s", decoder.getClass()));
                        }
                    }
                }
            });

            JsonObject enumProperty = new JsonObject();
            enumProperty.add("enum", enumArray);
            properties.add(typeFieldName, enumProperty);
        }
        else {
            properties.add(typeFieldName, new JsonObject());
        }

        if (context.debugMode) Codec2Schema.LOGGER.info("Finished dispatch: {}", typeFieldName);
    }
}
