package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

@ApiStatus.Experimental
public interface SchemaModifier<T> {
    @Contract(pure = true)
    boolean shouldApplyTo(T codec, ModificationStage stage);

    JsonObject apply(T codec, JsonObject json);

    enum ModificationStage {
        REQUEST,
        DEFINITION
    }
}
