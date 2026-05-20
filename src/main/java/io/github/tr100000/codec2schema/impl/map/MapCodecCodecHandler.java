package io.github.tr100000.codec2schema.impl.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tr100000.codec2schema.api.CodecHandler;
import io.github.tr100000.codec2schema.api.JsonUtils;
import io.github.tr100000.codec2schema.api.MapCodecHandler;
import io.github.tr100000.codec2schema.api.MapCodecHandlerRegistry;
import io.github.tr100000.codec2schema.api.SchemaContext;
import io.github.tr100000.codec2schema.api.Utils;
import io.github.tr100000.codec2schema.api.codec.WrappedMapCodec;
import io.github.tr100000.codec2schema.mixin.RecordCodecBuilderAccessor;

import java.util.List;
import java.util.Objects;

public class MapCodecCodecHandler implements CodecHandler<MapCodec.MapCodecCodec<?>> {
    public static boolean predicate(Codec<?> codec) {
        return codec instanceof MapCodec.MapCodecCodec<?>;
    }

    public static MapCodec<?> actually(MapCodec<?> codec) {
        if (codec instanceof WrappedMapCodec<?> wrappedMapCodec && !wrappedMapCodec.isUnitCodec()) {
            return actually(wrappedMapCodec.original());
        }
        return codec;
    }

    @Override
    public JsonObject toSchema(MapCodec.MapCodecCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        return toSchema(codec.codec(), context, definitionContext);
    }

    public static JsonObject toSchema(MapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        codec = actually(codec);
        if (codec instanceof WrappedRecordCodec<?> wrappedRecordCodec) {
            return recordCodecSchema(wrappedRecordCodec, context, definitionContext);
        }
        else if (codec instanceof KeyDispatchCodec<?, ?> keyDispatchCodec) {
            return KeyDispatchCodecHandler.createSchema(keyDispatchCodec, context, definitionContext);
        }
        else if (codec instanceof WrappedDispatchOptionalValueMapCodec<?,?> wrappedDispatchOptionalValueMapCodec) {
            return WrappedDispatchOptionalValueMapCodecHandler.createSchema(wrappedDispatchOptionalValueMapCodec, context, definitionContext);
        }
        else if (Utils.isRecursiveMapCodec(codec)) {
            JsonObject json = context.createRef(definitionContext.definitionStack().get(1));
            context.cancel(definitionContext.name().orElseThrow());
            return json;
        }
        else {
            MapCodecHandler<MapCodec<?>> handler = MapCodecHandlerRegistry.getHandlerOrThrow(codec);
            return handler.toSchema(codec, context, definitionContext);
        }
    }

    private static JsonObject recordCodecSchema(WrappedRecordCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        JsonObject json = new JsonObject();
        JsonObject properties = new JsonObject();
        JsonArray required = new JsonArray();

        json.addProperty("type", "object");
        List<RecordCodecBuilder<?, ?>> fields = codec.fields();
        for (var field : fields) {
            handleField(json, properties, required, getFieldCodec(field), context, definitionContext);
        }

        if (!properties.isEmpty()) json.add("properties", properties);
        if (!required.isEmpty()) json.add("required", required);
        return json;
    }

    private static MapCodec<?> getFieldCodec(RecordCodecBuilder<?, ?> codecBuilder) {
        return actually((MapCodec<?>)((RecordCodecBuilderAccessor<?>)(Object)codecBuilder).getDecoder());
    }

    private static void handleField(JsonObject json, JsonObject properties, JsonArray required, MapCodec<?> codec, SchemaContext context, SchemaContext.DefinitionContext definitionContext) {
        if (Objects.requireNonNull(codec) instanceof WrappedRecordCodec<?> wrappedRecordCodec) {
            JsonUtils.getOrCreateArray(json, "allOf").add(context.requestDefinition(wrappedRecordCodec.codec()));
        } else if (Utils.isRecursiveMapCodec(codec)) {
            JsonUtils.getOrCreateArray(json, "allOf").add(context.createRef(definitionContext.name().orElseThrow()));
        } else {
            MapCodecHandler<MapCodec<?>> handler = MapCodecHandlerRegistry.getHandlerOrThrow(codec);
            handler.field(json, properties, required, codec, context, definitionContext);
        }
    }
}
