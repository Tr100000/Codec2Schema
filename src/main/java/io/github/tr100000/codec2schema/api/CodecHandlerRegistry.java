package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.Codec2Schema;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class CodecHandlerRegistry {
    private CodecHandlerRegistry() {}

    private static final List<Entry<?>> ENTRIES = new ObjectArrayList<>();
    private static final List<SchemaModifier<? extends Codec<?>>> MODIFIERS = new ObjectArrayList<>();

    public static <T extends Codec<?>> void register(Predicate<Codec<?>> predicate, Function<T, ? extends CodecHandler<T>> factory) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(factory, "factory is null");
        ENTRIES.add(new Entry<>(predicate, factory));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Codec<?>> void register(Predicate<Codec<?>> predicate, Supplier<CodecHandler<T>> factory) {
        register(predicate, (Function)(ignored -> factory.get()));
    }

    public static void registerModifier(SchemaModifier<? extends Codec<?>> modifier) {
        Objects.requireNonNull(modifier, "modifier is null");
        MODIFIERS.add(modifier);
    }

    @SuppressWarnings("unchecked")
    @Contract(pure = true)
    public static <T extends Codec<?>> CodecHandler<T> getHandlerOrThrow(T codec) {
        for (Entry<?> entry : ENTRIES) {
            if (entry.predicate().test(codec)) {
                try {
                    CodecHandler<T> handler = ((Entry<T>)entry).factory().apply(codec);
                    if (handler != null) return handler;
                }
                catch (Exception e) {
                    Codec2Schema.LOGGER.warn("Error while attempting to retrieve codec handler for {} with entry {}", codec.getClass(), entry, e);
                }
            }
        }
        throw new IllegalStateException(String.format("No codec handler found for %s", codec.getClass().getName()));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ApiStatus.Experimental
    public static JsonObject applyModifiers(Codec<?> codec, JsonObject json, SchemaModifier.ModificationStage stage, boolean listUsedModifiersInJson) {
        for (SchemaModifier modifier : MODIFIERS) {
            if (modifier.shouldApplyTo(codec, stage)) {
                json = modifier.apply(codec, json);

                if (listUsedModifiersInJson) {
                    JsonUtils.getOrCreateArray(json, "_modifiers").add(modifier.getClass().toString());
                }

                Codec2Schema.LOGGER.info("Applied modifier {}", modifier.getClass());
            }
        }
        return json;
    }

    private record Entry<T extends Codec<?>>(Predicate<Codec<?>> predicate, Function<T, ? extends @Nullable CodecHandler<T>> factory) {}
}
