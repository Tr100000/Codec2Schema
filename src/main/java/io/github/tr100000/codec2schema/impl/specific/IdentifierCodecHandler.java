package io.github.tr100000.codec2schema.impl.specific;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class IdentifierCodecHandler implements CodecHandler<Codec<?>> {
    public static final String PATTERN = "([_\\-a-z0-9.]+:)?[_\\-a-z0-9/.]+";

    public static boolean predicate(Codec<?> codec) {
        return codec == Identifier.CODEC;
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "string");
        json.addProperty("pattern", PATTERN);
        return json;
    }

    @Override
    public Optional<String> getName(Codec<?> codec) {
        return Optional.of("identifier");
    }
}
