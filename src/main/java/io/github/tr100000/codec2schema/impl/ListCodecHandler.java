package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.ExtraCodecs;

import java.util.Map;
import java.util.Optional;

public class ListCodecHandler implements CodecHandler<Codec<?>> {
    protected static final Map<Codec<?>, OverrideEntry> SIZE_OVERRIDES = new Object2ObjectOpenHashMap<>();

    public static boolean predicate(Codec<?> codec) {
        return codec instanceof ListCodec<?> || SIZE_OVERRIDES.containsKey(codec);
    }

    @Override
    public JsonObject toSchema(Codec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        if (codec instanceof ListCodec<?> listCodec) {
            return toSchema(listCodec.elementCodec(), minSize(listCodec), maxSize(listCodec), context);
        }
        else {
            OverrideEntry entry = SIZE_OVERRIDES.get(codec);
            return toSchema(entry.elementCodec, entry.minSize, entry.maxSize, context);
        }
    }

    public JsonObject toSchema(Codec<?> codec, Optional<Integer> min, Optional<Integer> max, SchemaContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "array");
        json.add("items", context.requestDefinition(codec));
        min.ifPresent(integer -> json.addProperty("minItems", integer));
        max.ifPresent(integer -> json.addProperty("maxItems", integer));
        return json;
    }

    private Optional<Integer> minSize(ListCodec<?> codec) {
        return codec.minSize() > 0 ? Optional.of(codec.minSize()) : Optional.empty();
    }

    private Optional<Integer> maxSize(ListCodec<?> codec) {
        return codec.maxSize() < Integer.MAX_VALUE ? Optional.of(codec.maxSize()) : Optional.empty();
    }

    @Override
    public boolean shouldInline(Codec<?> codec) {
        return true;
    }

    public static void registerOverride(Codec<?> codecToOverride, Codec<?> elementCodec, Optional<Integer> minSize, Optional<Integer> maxSize) {
        SIZE_OVERRIDES.put(codecToOverride, new OverrideEntry(elementCodec, minSize, maxSize));
    }

    private record OverrideEntry(Codec<?> elementCodec, Optional<Integer> minSize, Optional<Integer> maxSize) {}

    static {
        registerOverride(ExtraCodecs.VECTOR2F, Codec.FLOAT, Optional.of(2), Optional.of(2));
        registerOverride(ExtraCodecs.VECTOR3F, Codec.FLOAT, Optional.of(3), Optional.of(3));
        registerOverride(ExtraCodecs.VECTOR3I, Codec.INT, Optional.of(3), Optional.of(3));
        registerOverride(ExtraCodecs.VECTOR4F, Codec.FLOAT, Optional.of(4), Optional.of(4));
        registerOverride(ExtraCodecs.QUATERNIONF_COMPONENTS, Codec.FLOAT, Optional.of(4), Optional.of(4));
        registerOverride(ExtraCodecs.MATRIX4F, Codec.FLOAT, Optional.of(16), Optional.of(16));
    }
}
