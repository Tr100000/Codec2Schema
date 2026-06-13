package io.github.tr100000.codec2schema.impl;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.SchemaContext;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.levelgen.Xoroshiro128PlusPlus;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Optional;

public class ListCodecHandler implements CodecHandler<Codec<?>> {
    private static final Map<Codec<?>, OverrideEntry> SIZE_OVERRIDES = new Object2ObjectOpenHashMap<>();

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

    public static JsonObject toSchema(Codec<?> codec, Optional<Integer> min, Optional<Integer> max, SchemaContext context) {
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

    public static void registerOverride(Codec<?> codecToOverride, Codec<?> elementCodec, int fixedSize) {
        registerOverride(codecToOverride, elementCodec, Optional.of(fixedSize), Optional.of(fixedSize));
    }

    private record OverrideEntry(Codec<?> elementCodec, Optional<Integer> minSize, Optional<Integer> maxSize) {}

    static {
        registerOverride(Rotations.CODEC, Codec.FLOAT, 3);
        registerOverride(ExtraCodecs.VECTOR2F, Codec.FLOAT, 2);
        registerOverride(ExtraCodecs.VECTOR3F, Codec.FLOAT, 3);
        registerOverride(ExtraCodecs.VECTOR3I, Codec.INT, 3);
        registerOverride(ExtraCodecs.VECTOR4F, Codec.FLOAT, 4);
        registerOverride(ExtraCodecs.QUATERNIONF_COMPONENTS, Codec.FLOAT, 4);
        registerOverride(ExtraCodecs.MATRIX4F, Codec.FLOAT, 16);
        registerOverride(Vec2.CODEC, Codec.FLOAT, 2);
        registerOverride(Vec3.CODEC, Codec.FLOAT, 3);

        registerOverride(BlockPos.CODEC, Codec.INT, 3);
        registerOverride(UUIDUtil.CODEC, Codec.INT, 4);
        registerOverride(Vec3i.CODEC, Codec.INT, 3);
        registerOverride(ChunkPos.CODEC, Codec.INT, 2);
        registerOverride(BoundingBox.CODEC, Codec.INT, 6);

        registerOverride(Xoroshiro128PlusPlus.CODEC, Codec.LONG, 2);

        registerOverride(SignText.LINES_CODEC, ComponentSerialization.CODEC, 4);
    }
}
