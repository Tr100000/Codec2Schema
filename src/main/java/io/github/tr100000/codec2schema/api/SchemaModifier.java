package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface SchemaModifier<T> {
    boolean shouldApplyTo(T codec, ModificationStage stage);
    JsonObject apply(T codec, JsonObject json);

    enum ModificationStage {
        REQUEST,
        DEFINITION
    }
}
