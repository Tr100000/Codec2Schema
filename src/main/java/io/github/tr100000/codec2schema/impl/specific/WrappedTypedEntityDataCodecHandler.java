package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.nbt.TagParser;

/**
 * @see net.minecraft.world.item.component.TypedEntityData#codec
 */
public class WrappedTypedEntityDataCodecHandler implements CodecHandler<WrappedTypedEntityDataCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedTypedEntityDataCodec<?>;
    }

    @Override
    public JsonObject toSchema(WrappedTypedEntityDataCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        JsonArray anyOf = JsonUtils.getOrCreateArray(json, "anyOf");

        anyOf.add(context.requestDefinition(TagParser.FLATTENED_CODEC));

        JsonObject objSchema = new JsonObject();
        objSchema.addProperty("type", "object");
        JsonUtils.getOrCreateObject(objSchema, "properties").add("id", context.requestDefinition(codec.typeCodec()));
        JsonUtils.getOrCreateArray(objSchema, "required").add("id");
        anyOf.add(objSchema);

        return json;
    }
}
