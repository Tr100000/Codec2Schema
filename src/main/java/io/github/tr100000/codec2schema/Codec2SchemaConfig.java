package io.github.tr100000.codec2schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public record Codec2SchemaConfig(boolean defaultDebugMode, boolean defaultAllowInline, List<PluginMatch> pluginsToDisable, @ApiStatus.Experimental boolean inlineSingularReference) {
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("codec2schema.json");
    public static final Codec<Codec2SchemaConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            optionalField(Codec.BOOL, "defaultDebugMode", false).forGetter(Codec2SchemaConfig::defaultDebugMode),
            optionalField(Codec.BOOL, "defaultAllowInline", true).forGetter(Codec2SchemaConfig::defaultAllowInline),
            optionalField(PluginMatch.CODEC.listOf(), "pluginsToDisable", List.of()).forGetter(Codec2SchemaConfig::pluginsToDisable),
            optionalField(Codec.BOOL, "inlineSingularReference", false).forGetter(Codec2SchemaConfig::inlineSingularReference)
    ).apply(instance, Codec2SchemaConfig::new));

    private static <A> MapCodec<A> optionalField(Codec<A> codec, String name, A defaultValue) {
        return codec.optionalFieldOf(name).xmap(o -> o.orElse(defaultValue), Optional::ofNullable);
    }

    public static Codec2SchemaConfig INSTANCE = new Codec2SchemaConfig(true, false, List.of(), false);

    public boolean shouldRunPlugin(String modid, PluginSide side) {
        return pluginsToDisable.stream().noneMatch(p -> p.matches(modid, side));
    }

    public static void load() {
        try {
            if (Files.notExists(PATH)) {
                save();
                return;
            }

            JsonObject json = Codec2Schema.GSON.fromJson(Files.readString(PATH), JsonObject.class);
            INSTANCE = CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();

            save();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load Codec2SChema config!", e);
        }
    }

    public static void save() {
        try {
            Files.deleteIfExists(PATH);

            JsonElement json = CODEC.encodeStart(JsonOps.INSTANCE, INSTANCE).getPartialOrThrow();
            Files.writeString(PATH, Codec2Schema.GSON.toJson(json));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load Codec2SChema config!", e);
        }
    }

    public interface PluginMatch {
        Codec<PluginMatch> CODEC = Codec.either(SidedPluginMatch.CODEC, ModPluginMatch.CODEC)
                .flatComapMap(
                        Either::unwrap,
                        pluginMatch -> switch (pluginMatch) {
                            case SidedPluginMatch sided -> DataResult.success(Either.left(sided));
                            case ModPluginMatch mod -> DataResult.success(Either.right(mod));
                            default -> DataResult.error(() -> "uh oh");
                        }
                );

        boolean matches(String modid, PluginSide side);
    }

    private record SidedPluginMatch(String modid, PluginSide side) implements PluginMatch {
        public static final Codec<SidedPluginMatch> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("mod").forGetter(SidedPluginMatch::modid),
                PluginSide.CODEC.fieldOf("side").forGetter(SidedPluginMatch::side)
        ).apply(instance, SidedPluginMatch::new));

        @Override
        public boolean matches(String modid, PluginSide side) {
            return this.modid.equals(modid) && this.side == side;
        }
    }

    private record ModPluginMatch(String modid) implements PluginMatch {
        public static final Codec<ModPluginMatch> CODEC = Codec.STRING.xmap(ModPluginMatch::new, ModPluginMatch::modid);

        @Override
        public boolean matches(String modid, PluginSide side) {
            return this.modid.equals(modid);
        }
    }
}
