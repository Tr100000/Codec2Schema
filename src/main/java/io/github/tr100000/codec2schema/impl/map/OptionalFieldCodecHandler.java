package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.OptionalFieldCodec;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.mixin.OptionalFieldCodecAccessor;

public class OptionalFieldCodecHandler implements MapCodecHandler<MapCodec<?>> {
    public static boolean predicate(MapCodec<?> codec) {
        return codec instanceof OptionalFieldCodec<?> || codec instanceof WrappedDefaultedOptionalFieldMapCodec<?>;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void field(JsonObject json, JsonObject properties, JsonArray required, MapCodec<?> c, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        String name;
        JsonObject fieldJson;
        switch (c) {
            case WrappedDefaultedOptionalFieldMapCodec<?> codec -> {
                name = codec.getName();
                fieldJson = context.requestDefinition(codec.getElementCodec());
                ((Codec<Object>)codec.getElementCodec()).encodeStart(JsonOps.INSTANCE, codec.defaultValue()).ifSuccess(d -> fieldJson.add("default", d));
            }
            case OptionalFieldCodec<?> codec -> {
                OptionalFieldCodecAccessor<?> accessor = (OptionalFieldCodecAccessor<?>)codec;
                name = accessor.getName();
                fieldJson = context.requestDefinition(accessor.getElementCodec());
            }
            default -> throw new IllegalStateException();
        }
        properties.add(name, fieldJson);
    }
}
