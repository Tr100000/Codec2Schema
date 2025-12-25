package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.Utils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;

public class WrappedStateHolderCodecHandler implements CodecHandler<WrappedStateHolderCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof WrappedStateHolderCodec<?>;
    }

    @Override
    public JsonObject toSchema(WrappedStateHolderCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "object");
        JsonObject properties = JsonUtils.getOrCreateObject(json, "properties");

        JsonArray enumArray = new JsonArray();
        JsonArray allOfArray = JsonUtils.getOrCreateArray(json, "allOf");

        Utils.getPossibleValues(codec.dispatchedCodec()).orElseThrow().forEach(pair -> {
            StateHolder<?, ?> stateHolder = codec.toStateHolderFunction().apply(pair.value());
            enumArray.add(pair.str());
            if (!stateHolder.getProperties().isEmpty()) {
                allOfArray.add(JsonUtils.schemaIfPropertyEquals("Name", pair.str(), getPropertiesCodec(stateHolder.getProperties(), context)));
            }
        });

        JsonObject nameProperty = new JsonObject();
        JsonArray nameAnyOf = JsonUtils.getOrCreateArray(nameProperty, "anyOf");
        nameAnyOf.add(context.requestDefinition(Identifier.CODEC));
        JsonObject enumEntry = new JsonObject();
        enumEntry.add("enum", enumArray);
        nameAnyOf.add(enumEntry);
        properties.add("Name", nameProperty);

        return json;
    }

    private JsonObject getPropertiesCodec(Collection<Property<?>> propertyCollection, SchemaContext context) {
        JsonObject json = new JsonObject();
        JsonObject properties = JsonUtils.getOrCreateObject(json, "properties");

        propertyCollection.forEach(property -> properties.add(property.getName(), context.requestDefinition(property.codec())));

        return json;
    }
}
