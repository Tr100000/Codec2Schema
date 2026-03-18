package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;

public interface CodecSchemaModifier {
    boolean shouldApplyTo(Codec<?> codec);
    JsonObject apply(Codec<?> codec, JsonObject json);
}
