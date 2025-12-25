package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.impl.specific.IdentifierCodecHandler;
import io.github.tr100000.codec2schema.mixin.HolderSetCodecAccessor;
import net.minecraft.resources.HolderSetCodec;

public record HolderSetCodecHandler(HolderSetCodec<?> codec) implements CodecHandler<HolderSetCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof HolderSetCodec<?>;
    }

    @Override
    public JsonObject toSchema(HolderSetCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        JsonArray anyOf = new JsonArray();

        JsonObject tagJson = new JsonObject();
        tagJson.addProperty("type", "string");
        tagJson.addProperty("pattern", "#" + IdentifierCodecHandler.PATTERN);

        JsonObject entryJson = context.requestDefinition(((HolderSetCodecAccessor)codec).getElementCodec());

        JsonObject entryArrayJson = new JsonObject();
        entryArrayJson.addProperty("type", "array");
        entryArrayJson.add("items", entryJson);

        anyOf.add(tagJson);
        anyOf.add(entryJson);
        anyOf.add(entryArrayJson);
        json.add("anyOf", anyOf);
        return json;
    }
}
