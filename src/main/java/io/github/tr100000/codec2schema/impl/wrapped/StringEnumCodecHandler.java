package io.github.tr100000.codec2schema.impl.wrapped;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;

public record StringEnumCodecHandler(String[] enumValues) implements CodecHandler<Codec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedEnumCodec<?> || codec instanceof WrappedStringRepresentableCodec<?>;
    }

    public static StringEnumCodecHandler createHandler(Codec<?> codec) {
        return switch (codec) {
            case WrappedEnumCodec<?> wrappedEnumCodec ->
                    new StringEnumCodecHandler(wrappedEnumCodec.getEnumValues());
            case WrappedStringRepresentableCodec<?> wrappedStringRepresentableCodec ->
                    new StringEnumCodecHandler(wrappedStringRepresentableCodec.getEnumValues());
            default -> throw new IllegalArgumentException("Invalid argument of type " + codec.getClass());
        };
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context) {
        JsonObject json = new JsonObject();
        JsonArray enumArray = new JsonArray();
        for (String enumValue : enumValues) {
            enumArray.add(enumValue);
        }
        json.add("enum", enumArray);
        return json;
    }
}
