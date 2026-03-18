package io.github.tr100000.codec2schema.api;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.Codec2Schema;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class CodecHandlerRegistry {
    private CodecHandlerRegistry() {}

    private static final List<Entry<?>> ENTRIES = new ObjectArrayList<>();
    private static final List<CodecSchemaModifier> MODIFIERS = new ObjectArrayList<>();

    public static <T extends Codec<?>> void register(Predicate<Codec<?>> predicate, Function<T, ? extends CodecHandler<T>> factory) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(factory, "factory is null");
        ENTRIES.add(new Entry<>(predicate, factory));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Codec<?>> void register(Predicate<Codec<?>> predicate, Supplier<CodecHandler<T>> factory) {
        register(predicate, (Function)(ignored -> factory.get()));
    }

    public static void registerModifier(CodecSchemaModifier modifier) {
        Objects.requireNonNull(modifier, "modifier is null");
        MODIFIERS.add(modifier);
    }

    @SuppressWarnings("unchecked")
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

    public static JsonObject applyModifiers(Codec<?> codec, JsonObject json) {
        for (CodecSchemaModifier modifier : MODIFIERS) {
            if (modifier.shouldApplyTo(codec)) {
                json = modifier.apply(codec, json);
            }
        }
        return json;
    }

    private record Entry<T extends Codec<?>>(Predicate<Codec<?>> predicate, Function<T, ? extends @Nullable CodecHandler<T>> factory) {}
}
