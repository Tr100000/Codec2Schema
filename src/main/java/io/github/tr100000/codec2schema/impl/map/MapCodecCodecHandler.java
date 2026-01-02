package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.codec2schema.Codec2Schema;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.MapCodecHandlerRegistry;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.Utils;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.api.codec.WrappedMapCodec;
import io.github.tr100000.codec2schema.mixin.KeyDispatchCodecAccessor;
import io.github.tr100000.codec2schema.mixin.RecordCodecBuilderAccessor;

import java.util.List;
import java.util.Optional;

public class MapCodecCodecHandler implements CodecHandler<MapCodec.MapCodecCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof MapCodec.MapCodecCodec<?>;
    }

    public static MapCodec<?> actually(MapCodec<?> codec) {
        if (codec instanceof WrappedMapCodec<?> wrappedMapCodec && !wrappedMapCodec.isUnitCodec()) {
            return actually(wrappedMapCodec.original());
        }
        return codec;
    }

    @Override
    public JsonObject toSchema(MapCodec.MapCodecCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return toSchema(codec.codec(), context, definitionContext);
    }

    public static JsonObject toSchema(MapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        codec = actually(codec);
        if (codec instanceof WrappedRecordCodec<?> wrappedRecordCodec) {
            return recordCodecSchema(wrappedRecordCodec, context, definitionContext);
        }
        else if (codec instanceof KeyDispatchCodec<?, ?> keyDispatchCodec) {
            return keyDispatchCodecSchema(keyDispatchCodec, context);
        }
        else if (codec instanceof WrappedDispatchOptionalValueMapCodec<?,?> wrappedDispatchOptionalValueMapCodec) {
            return wrappedDispatchOptionalValueMapCodecField(wrappedDispatchOptionalValueMapCodec, context);
        }
        else if (Utils.isRecursiveMapCodec(codec)) {
            JsonObject json = context.createRef(definitionContext.definitionStack().get(1));
            context.cancel(definitionContext.name().orElseThrow());
            return json;
        }
        else {
            MapCodecHandler<MapCodec<?>> handler = MapCodecHandlerRegistry.getHandlerOrThrow(codec);
            return handler.toSchema(codec, context, definitionContext);
        }
    }

    private static JsonObject recordCodecSchema(WrappedRecordCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        JsonObject properties = new JsonObject();
        JsonArray required = new JsonArray();

        json.addProperty("type", "object");
        List<RecordCodecBuilder<?, ?>> fields = codec.fields();
        for (var field : fields) {
            handleField(json, properties, required, getFieldCodec(field), context, definitionContext);
        }

        if (!properties.isEmpty()) json.add("properties", properties);
        if (!required.isEmpty()) json.add("required", required);
        return json;
    }

    private static MapCodec<?> getFieldCodec(RecordCodecBuilder<?, ?> codecBuilder) {
        return actually((MapCodec<?>)((RecordCodecBuilderAccessor<?>)(Object)codecBuilder).getDecoder());
    }

    private static void handleField(JsonObject json, JsonObject properties, JsonArray required, MapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        switch (codec) {
            case WrappedRecordCodec<?> wrappedRecordCodec -> {
                JsonUtils.getOrCreateArray(json, "allOf").add(context.requestDefinition(wrappedRecordCodec.codec()));
            }
            case KeyDispatchCodec<?, ?> keyDispatchCodec -> {
                keyDispatchField(json, properties, required, keyDispatchCodec, context);
            }
            case WrappedDispatchOptionalValueMapCodec<?, ?> wrappedDispatchOptionalValueMapCodec -> {
                wrappedDispatchOptionalValueMapCodecSchema(json, properties, required, wrappedDispatchOptionalValueMapCodec, context);
            }
            default -> {
                if (Utils.isRecursiveMapCodec(codec)) {
                    JsonUtils.getOrCreateArray(json, "allOf").add(context.createRef(definitionContext.name().orElseThrow()));
                }
                else {
                    MapCodecHandler<MapCodec<?>> handler = MapCodecHandlerRegistry.getHandlerOrThrow(codec);
                    handler.field(json, properties, required, codec, context, definitionContext);
                }
            }
        }
    }

    private static <K, V> JsonObject keyDispatchCodecSchema(KeyDispatchCodec<K, V> codec, SchemaContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        JsonObject properties = JsonUtils.getOrCreateObject(json, "properties");
        JsonArray required = JsonUtils.getOrCreateArray(json, "required");
        keyDispatchField(json, properties, required, codec, context);
        return json;
    }

    @SuppressWarnings("unchecked")
    private static <K, V> void keyDispatchField(JsonObject json, JsonObject properties, JsonArray required, KeyDispatchCodec<K, V> codec, SchemaContext context) {
        MapCodec<K> keyCodec = ((KeyDispatchCodecAccessor<K, V>)codec).getKeyCodec();
        String typeFieldName = Utils.getFieldNameForDispatch(actually(keyCodec), required::add);
        required.add(typeFieldName);

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

    private static <K, V> JsonObject wrappedDispatchOptionalValueMapCodecField(WrappedDispatchOptionalValueMapCodec<K, V> codec, SchemaContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        JsonObject properties = JsonUtils.getOrCreateObject(json, "properties");
        JsonArray required = JsonUtils.getOrCreateArray(json, "required");
        wrappedDispatchOptionalValueMapCodecSchema(json, properties, required, codec, context);
        return json;
    }

    private static <K, V> void wrappedDispatchOptionalValueMapCodecSchema(JsonObject json, JsonObject properties, JsonArray required, WrappedDispatchOptionalValueMapCodec<K, V> codec, SchemaContext context) {
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
