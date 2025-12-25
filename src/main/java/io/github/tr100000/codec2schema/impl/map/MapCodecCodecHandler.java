package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.codecs.EitherMapCodec;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import com.mojang.serialization.codecs.OptionalFieldCodec;
import com.mojang.serialization.codecs.PairMapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.SimpleMapCodec;
import io.github.tr100000.codec2schema.Codec2Schema;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.StringValuePair;
import io.github.tr100000.codec2schema.api.Utils;
import io.github.tr100000.codec2schema.api.WrappedMapCodec;
import io.github.tr100000.codec2schema.mixin.EitherMapCodecAccessor;
import io.github.tr100000.codec2schema.mixin.KeyDispatchCodecAccessor;
import io.github.tr100000.codec2schema.mixin.OptionalFieldCodecAccessor;
import io.github.tr100000.codec2schema.mixin.PairMapCodecAccessor;
import io.github.tr100000.codec2schema.mixin.RecordCodecBuilderAccessor;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
        if (codec instanceof WrappedMapCodec<?> wrappedMapCodec && wrappedMapCodec.isUnitCodec()) {
            return new JsonObject();
        }
        else if (codec instanceof SimpleMapCodec<?, ?> simpleMapCodec) {
            return simpleMapCodecSchema(simpleMapCodec);
        }
        else if (codec instanceof WrappedRecordCodec<?> wrappedRecordCodec) {
            return recordCodecSchema(wrappedRecordCodec, context, definitionContext);
        }
        else if (codec instanceof KeyDispatchCodec<?, ?> keyDispatchCodec) {
            return keyDispatchCodecSchema(keyDispatchCodec, context);
        }
        else if (codec instanceof EitherMapCodec<?, ?> eitherMapCodec) {
            return eitherMapCodecSchema(eitherMapCodec, context);
        }
        else if (codec instanceof PairMapCodec<?, ?> pairMapCodec) {
            return pairMapCodecSchema(pairMapCodec, context);
        }
        else if (codec instanceof WrappedDispatchOptionalValueMapCodec<?,?> wrappedDispatchOptionalValueMapCodec) {
            return wrappedDispatchOptionalValueMapCodecField(wrappedDispatchOptionalValueMapCodec, context);
        }
        else if (codec instanceof WrappedFieldMapCodec<?> wrappedFieldMapCodec) {
            return singlePropertySchema(wrappedFieldMapCodec.fieldName(), wrappedFieldMapCodec.original(), true, context);
        }
        else if (codec instanceof OptionalFieldCodec<?> optionalFieldCodec) {
            OptionalFieldCodecAccessor accessor = (OptionalFieldCodecAccessor)optionalFieldCodec;
            return singlePropertySchema(accessor.getName(), accessor.getElementCodec(), false, context);
        }
        else if (ComponentSerializationCodecUtils.canHandle(codec)) {
            return ComponentSerializationCodecUtils.toSchema(codec, context);
        }
        else if (Utils.isRecursiveMapCodec(codec)) {
            JsonObject json = context.createRef(definitionContext.definitionStack().get(1));
            context.cancel(definitionContext.name().orElseThrow());
            return json;
        }

        throw new IllegalArgumentException(String.format("No map codec handler for %s", codec.getClass()));
    }

    private static JsonObject singlePropertySchema(String fieldName, Codec<?> codec, boolean isRequired, SchemaContext context) {
        JsonObject json = new JsonObject();
        JsonObject properties = new JsonObject();
        properties.add(fieldName, context.requestDefinition(codec));
        json.add("properties", properties);
        if (isRequired) {
            JsonArray required = new JsonArray();
            required.add(fieldName);
            json.add("required", required);
        }
        return json;
    }

    private static JsonObject simpleMapCodecSchema(SimpleMapCodec<?, ?> codec) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        json.add("propertyNames", createEnumFromKeys(codec.keys(JsonOps.INSTANCE)));
        return json;
    }

    private static JsonObject createEnumFromKeys(Stream<JsonElement> validKeys) {
        JsonObject json = new JsonObject();
        JsonArray enumArray = new JsonArray();
        validKeys.forEachOrdered(enumArray::add);
        json.add("enum", enumArray);
        return json;
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
        return actually((MapCodec<?>)((RecordCodecBuilderAccessor)(Object)codecBuilder).getDecoder());
    }

    private static void handleField(JsonObject json, JsonObject properties, JsonArray required, MapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        switch (codec) {
            case WrappedFieldMapCodec<?> wrappedFieldCodec -> {
                properties.add(wrappedFieldCodec.fieldName(), context.requestDefinition(wrappedFieldCodec.original()));
                required.add(wrappedFieldCodec.fieldName());
            }
            case OptionalFieldCodec<?> optionalFieldCodec -> {
                OptionalFieldCodecAccessor accessor = (OptionalFieldCodecAccessor)optionalFieldCodec;
                properties.add(accessor.getName(), context.requestDefinition(accessor.getElementCodec()));
            }
            case EitherMapCodec<?, ?> eitherMapCodec -> {
                JsonArray anyOf = new JsonArray();
                EitherMapCodecAccessor accessor = (EitherMapCodecAccessor)(Object)eitherMapCodec;
                anyOf.add(context.requestDefinition(accessor.getFirst().codec()));
                anyOf.add(context.requestDefinition(accessor.getSecond().codec()));

                JsonObject entry = new JsonObject();
                entry.add("anyOf", anyOf);
                JsonUtils.getOrCreateArray(json, "allOf").add(entry);
            }
            case WrappedRecordCodec<?> wrappedRecordCodec -> {
                JsonUtils.getOrCreateArray(json, "allOf").add(context.requestDefinition(wrappedRecordCodec.codec()));
            }
            case KeyDispatchCodec<?, ?> keyDispatchCodec -> {
                keyDispatchField(json, properties, required, keyDispatchCodec, context);
            }
            case WrappedDispatchOptionalValueMapCodec<?, ?> wrappedDispatchOptionalValueMapCodec -> {
                wrappedDispatchOptionalValueMapCodecSchema(json, properties, required, wrappedDispatchOptionalValueMapCodec, context);
            }
            case ComponentSerialization.FuzzyCodec<?> fuzzyCodec -> {
                ComponentSerializationCodecUtils.fuzzyCodecSchema(json, fuzzyCodec, context);
            }
            case ComponentSerialization.StrictEither<?> strictEither -> {
                ComponentSerializationCodecUtils.strictEitherSchema(json, strictEither, context);
            }
            case WrappedMapCodec<?> wrappedMapCodec when wrappedMapCodec.isUnitCodec() -> {
                // do nothing
            }
            default -> {
                if (Utils.isRecursiveMapCodec(codec)) {
                    JsonUtils.getOrCreateArray(json, "allOf").add(context.createRef(definitionContext.name().orElseThrow()));
                }
                else {
                    throw new IllegalArgumentException(codec.getClass().toString());
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

        if (context.debugMode) Codec2Schema.LOGGER.info("Starting dispatch: {}", typeFieldName);

        Optional<List<StringValuePair<K>>> possibleValues = Utils.getPossibleValues(keyCodec);
        if (possibleValues.isPresent()) {
            JsonArray enumArray = new JsonArray();
            JsonArray allOfArray = JsonUtils.getOrCreateArray(json, "allOf");

            possibleValues.get().forEach(value -> {
                if (context.debugMode) Codec2Schema.LOGGER.info(value.str());
                MapDecoder<V> decoder = (MapDecoder<V>)((KeyDispatchCodecAccessor<K, V>)codec).getDecoder().apply(value.value()).getOrThrow();
                if (decoder instanceof MapCodec<V> mapCodec) {
                    enumArray.add(value.str());
                    allOfArray.add(JsonUtils.schemaIfPropertyEquals(typeFieldName, value.str(), context.requestDefinition(mapCodec.codec())));
                }
                else {
                    throw new IllegalStateException(String.format("Map decoder was of type %s", decoder.getClass()));
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
        if (context.debugMode) Codec2Schema.LOGGER.info("Starting dispatch: {} -> {}", codec.typeKey(), codec.dispatchKey());

        Codec<K> keyCodec = codec.keyCodec();
        Optional<List<StringValuePair<K>>> possibleValues = Utils.getPossibleValues(keyCodec);
        if (possibleValues.isPresent()) {
            JsonArray enumArray = new JsonArray();
            JsonArray allOfArray = JsonUtils.getOrCreateArray(json, "allOf");

            possibleValues.get().forEach(value -> {
                if (context.debugMode) Codec2Schema.LOGGER.info(value.str());

                JsonObject schema = new JsonObject();
                JsonObject schemaProperties = new JsonObject();
                schemaProperties.add(codec.dispatchKey(), context.requestDefinition(codec.valueCodecFunction().apply(value.value())));
                schema.add("properties", schemaProperties);

                enumArray.add(value.str());
                allOfArray.add(JsonUtils.schemaIfPropertyEquals(codec.typeKey(), value.str(), schema));
            });

            JsonObject enumProperty = new JsonObject();
            enumProperty.add("enum", enumArray);
            properties.add(codec.typeKey(), enumProperty);
        }
        else {
            properties.add(codec.typeKey(), new JsonObject());
        }

        if (context.debugMode) Codec2Schema.LOGGER.info("Finished dispatch: {} -> {}", codec.typeKey(), codec.dispatchKey());

        required.add(codec.typeKey());
    }

    private static JsonObject eitherMapCodecSchema(EitherMapCodec<?, ?> codec, SchemaContext context) {
        JsonObject json = new JsonObject();

        EitherMapCodecAccessor accessor = (EitherMapCodecAccessor)(Object)codec;
        JsonArray anyOf = new JsonArray();
        anyOf.add(context.requestDefinition(accessor.getFirst().codec()));
        anyOf.add(context.requestDefinition(accessor.getSecond().codec()));

        json.add("anyOf", anyOf);
        return json;
    }

    private static JsonObject pairMapCodecSchema(PairMapCodec<?, ?> codec, SchemaContext context) {
        JsonObject json = new JsonObject();

        PairMapCodecAccessor accessor = (PairMapCodecAccessor)(Object)codec;
        JsonArray allOf = new JsonArray();
        allOf.add(context.requestDefinition(accessor.getFirst().codec()));
        allOf.add(context.requestDefinition(accessor.getSecond().codec()));

        json.add("allOf", allOf);
        return json;
    }
}
