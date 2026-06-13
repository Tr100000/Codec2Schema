package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class CodecWithValuesHandler implements CodecHandler<CodecWithValuePairs<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof CodecWithValuePairs<?>;
    }

    @Override
    public JsonObject toSchema(CodecWithValuePairs<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        if (codec.getName().isPresent()) {
            return context.requestDefinition(codec.getName().get(), () -> createFromValues(context, codec.possibleValues().stream().map(ValueStringPair::str), codec.fallbackCodec()));
        }
        else {
            List<? extends ValueStringPair<?>> stringValues = codec.possibleValues();
            if (!stringValues.isEmpty()) {
                return createFromValues(context, stringValues.stream().map(ValueStringPair::str), codec.fallbackCodec());
            }
            return context.requestDefinition(codec.original());
        }
    }

    public static JsonObject createFromValues(SchemaContext context, Stream<String> values, @Nullable Codec<?> fallbackCodec) {
        JsonObject json = new JsonObject();
        JsonArray anyOf = new JsonArray();

        JsonObject enumObj = new JsonObject();
        JsonArray enumArray = new JsonArray();
        values.forEach(enumArray::add);
        enumObj.add("enum", enumArray);
        anyOf.add(enumObj);

        if (fallbackCodec != null) {
            anyOf.add(context.requestDefinition(fallbackCodec));
            json.add("anyOf", anyOf);
            return json;
        }
        else {
            return enumObj;
        }
    }

    @Override
    public boolean shouldInline(CodecWithValuePairs<?> codec) {
        return codec.getName().isPresent();
    }
}
