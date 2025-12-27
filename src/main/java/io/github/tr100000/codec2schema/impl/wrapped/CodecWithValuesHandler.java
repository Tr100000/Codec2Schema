package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;

import java.util.List;
import java.util.stream.Stream;

public class CodecWithValuesHandler implements CodecHandler<CodecWithValuePairs<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof CodecWithValuePairs<?>;
    }

    @Override
    public JsonObject toSchema(CodecWithValuePairs<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        if (codec.getName().isPresent()) {
            return context.requestDefinition(codec.getName().get(), () -> createFromValues(codec.possibleValues().stream().map(ValueStringPair::str)));
        }
        else {
            List<? extends ValueStringPair<?>> stringValues = codec.possibleValues();
            if (stringValues != null) {
                return createFromValues(stringValues.stream().map(ValueStringPair::str));
            }
            return context.requestDefinition(codec.original());
        }
    }

    public static JsonObject createFromValues(Stream<String> values) {
        JsonObject json = new JsonObject();
        JsonArray anyOf = new JsonArray();

        JsonObject enumObj = new JsonObject();
        JsonArray enumArray = new JsonArray();
        values.forEach(enumArray::add);
        enumObj.add("enum", enumArray);

        JsonObject strObj = new JsonObject();
        strObj.addProperty("type", "string");

        anyOf.add(enumObj);
        anyOf.add(strObj);
        json.add("anyOf", anyOf);
        return json;
    }

    @Override
    public boolean shouldInline(CodecWithValuePairs<?> codec) {
        return codec.getName().isPresent();
    }
}
