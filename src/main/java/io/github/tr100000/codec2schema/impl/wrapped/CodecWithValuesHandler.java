package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.CodecWithValues;
import io.github.tr100000.codec2schema.api.SchemaContext;

import java.util.List;

public class CodecWithValuesHandler implements CodecHandler<CodecWithValues<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof CodecWithValues<?>;
    }

    @Override
    public JsonObject toSchema(CodecWithValues<?> codec, SchemaContext context) {
        if (codec.getName().isPresent()) {
            return context.requestDefinition(codec.getName().get(), () -> createFromValues(codec.possibleStringValues()));
        }
        else {
            List<String> stringValues = codec.possibleStringValues();
            if (stringValues != null) {
                return createFromValues(stringValues);
            }
            return context.requestDefinition(codec.original());
        }
    }

    private JsonObject createFromValues(List<String> stringValues) {
        JsonObject json = new JsonObject();
        JsonArray anyOf = new JsonArray();

        JsonObject enumObj = new JsonObject();
        JsonArray enumArray = new JsonArray();
        stringValues.forEach(enumArray::add);
        enumObj.add("enum", enumArray);

        JsonObject strObj = new JsonObject();
        strObj.addProperty("type", "string");

        anyOf.add(enumObj);
        anyOf.add(strObj);
        json.add("anyOf", anyOf);
        return json;
    }
}
