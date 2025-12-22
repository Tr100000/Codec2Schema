package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.EitherMapCodec;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import com.mojang.serialization.codecs.OptionalFieldCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.SimpleMapCodec;
import io.github.tr100000.codec2schema.Codec2Schema;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.WrappedMapCodec;
import io.github.tr100000.codec2schema.mixin.EitherMapCodecAccessor;
import io.github.tr100000.codec2schema.mixin.KeyDispatchCodecAccessor;
import io.github.tr100000.codec2schema.mixin.OptionalFieldCodecAccessor;
import io.github.tr100000.codec2schema.mixin.RecordCodecBuilderAccessor;

import java.util.List;
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
    public JsonObject toSchema(MapCodec.MapCodecCodec<?> codec, SchemaContext context) {
        return toSchema(codec.codec(), context);
    }

    public JsonObject toSchema(MapCodec<?> codec, SchemaContext context) {
        codec = actually(codec);
        if (codec instanceof WrappedMapCodec<?> wrappedMapCodec && wrappedMapCodec.isUnitCodec()) {
            return new JsonObject();
        }
        else if (codec instanceof SimpleMapCodec<?, ?> simpleMapCodec) {
            return simpleMapCodecSchema(simpleMapCodec);
        }
        else if (codec instanceof WrappedRecordCodec<?> wrappedRecordCodec) {
            return recordCodecSchema(wrappedRecordCodec, context);
        }
        else if (codec instanceof KeyDispatchCodec<?, ?> keyDispatchCodec) {
            return keyDispatchCodecSchema(keyDispatchCodec, context);
        }
        else if (codec instanceof EitherMapCodec<?, ?> eitherMapCodec) {
            return eitherMapCodecSchema(eitherMapCodec, context);
        }

        throw new IllegalArgumentException(String.format("No map codec handler for %s", codec.getClass()));
    }

    private JsonObject simpleMapCodecSchema(SimpleMapCodec<?, ?> codec) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        json.add("propertyNames", createEnumFromKeys(codec.keys(JsonOps.INSTANCE)));
        return json;
    }

    private JsonObject createEnumFromKeys(Stream<JsonElement> validKeys) {
        JsonObject json = new JsonObject();
        JsonArray enumArray = new JsonArray();
        validKeys.forEachOrdered(enumArray::add);
        json.add("enum", enumArray);
        return json;
    }

    private JsonObject recordCodecSchema(WrappedRecordCodec<?> codec, SchemaContext context) {
        JsonObject json = new JsonObject();
        JsonObject properties = new JsonObject();
        JsonArray required = new JsonArray();

        json.addProperty("type", "object");
        List<RecordCodecBuilder<?, ?>> fields = codec.fields();
        for (var field : fields) {
            handleField(json, properties, required, getFieldCodec(field), context);
        }

        json.add("properties", properties);
        if (!required.isEmpty()) json.add("required", required);
        return json;
    }

    private MapCodec<?> getFieldCodec(RecordCodecBuilder<?, ?> codecBuilder) {
        return actually((MapCodec<?>)((RecordCodecBuilderAccessor)(Object)codecBuilder).getDecoder());
    }

    private void handleField(JsonObject json, JsonObject properties, JsonArray required, MapCodec<?> codec, SchemaContext context) {
        switch (codec) {
            case WrappedFieldMapCodec<?> wrappedFieldCodec -> {
                properties.add(wrappedFieldCodec.fieldName(), context.requestDefinition(wrappedFieldCodec.original()));
                required.add(wrappedFieldCodec.fieldName());
            }
            case OptionalFieldCodec<?> optionalFieldCodec -> {
                OptionalFieldCodecAccessor accessor = (OptionalFieldCodecAccessor) optionalFieldCodec;
                properties.add(accessor.getName(), context.requestDefinition(accessor.getElementCodec()));
            }
            case EitherMapCodec<?, ?> eitherMapCodec -> {
                // This doesn't work for some reason
//                JsonArray anyOf = new JsonArray();
//                EitherMapCodecAccessor accessor = (EitherMapCodecAccessor)(Object)eitherMapCodec;
//                anyOf.add(toSchema(accessor.getFirst(), context));
//                anyOf.add(toSchema(accessor.getSecond(), context));
//
//                JsonArray allOfArray = JsonUtils.getOrCreateArray(json, "allOf");
//                JsonObject entry = new JsonObject();
//                entry.add("anyOf", entry);
//                allOfArray.add(entry);
                Codec2Schema.LOGGER.warn("Ignore this (1)");
            }
            case WrappedRecordCodec<?> wrappedRecordCodec -> {
                // This also doesn't work for some reason
//                for (var field : wrappedRecordCodec.fields()) {
//                    handleField(json, properties, required, getFieldCodec(field), context);
//                }
                Codec2Schema.LOGGER.warn("Ignore this (2)");
            }
            default -> throw new IllegalArgumentException(codec.getClass().toString());
        }

    }

    private JsonObject keyDispatchCodecSchema(KeyDispatchCodec<?, ?> codec, SchemaContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        MapCodec<?> keyCodec = ((KeyDispatchCodecAccessor)codec).getKeyCodec();
        String typeFieldName = ((WrappedFieldMapCodec<?>)actually(keyCodec)).fieldName();

        JsonObject properties = new JsonObject();
        properties.add(typeFieldName, createEnumFromKeys(keyCodec.keys(JsonOps.INSTANCE)));
        // TODO this

        json.add("properties", properties);
        return json;
    }

    private JsonObject eitherMapCodecSchema(EitherMapCodec<?, ?> codec, SchemaContext context) {
        JsonObject json = new JsonObject();

        EitherMapCodecAccessor accessor = (EitherMapCodecAccessor)(Object)codec;
        JsonArray anyOf = new JsonArray();
        anyOf.add(toSchema(accessor.getFirst(), context));
        anyOf.add(toSchema(accessor.getSecond(), context));

        json.add("anyOf", anyOf);
        return json;
    }
}
