package io.github.tr100000.codec2schema.api;

import com.mojang.serialization.MapCodec;
import io.github.tr100000.codec2schema.Codec2Schema;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MapCodecHandlerRegistry {
    private MapCodecHandlerRegistry() {}

    private static final List<Entry<?>> ENTRIES = new ObjectArrayList<>();

    public static <T extends MapCodec<?>> void register(Predicate<MapCodec<?>> predicate, Function<? super T, ? extends MapCodecHandler<T>> factory) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(factory, "factory is null");
        ENTRIES.add(new Entry<>(predicate, factory));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends MapCodec<?>> void register(Predicate<MapCodec<?>> predicate, Supplier<MapCodecHandler<T>> factory) {
        register(predicate, (Function)(ignored -> factory.get()));
    }

    @SuppressWarnings("unchecked")
    public static <T extends MapCodec<?>> MapCodecHandler<T> getHandlerOrThrow(T codec) {
        for (Entry<?> entry : ENTRIES) {
            if (entry.predicate().test(codec)) {
                try {
                    MapCodecHandler<T> handler = ((Entry<T>)entry).factory().apply(codec);
                    if (handler != null) return handler;
                }
                catch (Exception e) {
                    Codec2Schema.LOGGER.warn("Error while attempting to handle map codec of type {} with entry {}", codec.getClass(), entry, e);
                }
            }
        }
        throw new IllegalStateException(String.format("No map codec handler found for %s", codec.getClass().getName()));
    }

    private record Entry<T extends MapCodec<?>>(Predicate<MapCodec<?>> predicate, Function<? super T, ? extends MapCodecHandler<T>> factory) {}
}
