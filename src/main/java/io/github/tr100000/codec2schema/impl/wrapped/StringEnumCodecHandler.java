package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;

public class StringEnumCodecHandler implements CodecHandler<CodecWithValuePairs<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedEnumCodec<?> || codec instanceof WrappedStringRepresentableCodec<?>;
    }

    @Override
    public JsonObject toSchema(CodecWithValuePairs<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        JsonArray enumArray = new JsonArray();
        codec.possibleValues().forEach(value -> enumArray.add(value.str()));
        json.add("enum", enumArray);
        return json;
    }
}
