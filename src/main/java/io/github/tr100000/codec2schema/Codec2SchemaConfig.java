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
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public record Codec2SchemaConfig(
        boolean defaultDebugMode,
        boolean defaultAllowInline,
        boolean addExportedWith,
        boolean exportPluginInfo,
        List<PluginMatch> pluginsToDisable,
        @ApiStatus.Experimental boolean inlineSingularReference
) {
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("codec2schema.json");
    public static final Codec<Codec2SchemaConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            optionalField(Codec.BOOL, "defaultDebugMode", false).forGetter(Codec2SchemaConfig::defaultDebugMode),
            optionalField(Codec.BOOL, "defaultAllowInline", true).forGetter(Codec2SchemaConfig::defaultAllowInline),
            optionalField(Codec.BOOL, "addExportedWith", false).forGetter(Codec2SchemaConfig::addExportedWith),
            optionalField(Codec.BOOL, "exportPluginInfo", false).forGetter(Codec2SchemaConfig::exportPluginInfo),
            optionalField(PluginMatch.CODEC.listOf(), "pluginsToDisable", List.of()).forGetter(Codec2SchemaConfig::pluginsToDisable),
            optionalField(Codec.BOOL, "inlineSingularReference", false).forGetter(Codec2SchemaConfig::inlineSingularReference)
    ).apply(instance, Codec2SchemaConfig::new));

    private static <A> MapCodec<A> optionalField(Codec<A> codec, String name, A defaultValue) {
        return codec.optionalFieldOf(name).xmap(o -> o.orElse(defaultValue), Optional::ofNullable);
    }

    public static Codec2SchemaConfig INSTANCE = CODEC.parse(JsonOps.INSTANCE, new JsonObject()).getOrThrow();

    public boolean shouldRunPlugin(Identifier pluginId, PluginSide side) {
        return pluginsToDisable.stream().noneMatch(p -> p.matches(pluginId, side));
    }

    public static void load() {
        try {
            if (Files.notExists(PATH)) {
                save();
                return;
            }

            JsonObject json = Codec2Schema.GSON.fromJson(Files.readString(PATH), JsonObject.class);
            INSTANCE = CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

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
        Codec<PluginMatch> CODEC = Codec.either(ModSidedPluginMatch.CODEC, IdPluginMatch.CODEC)
                .flatComapMap(
                        Either::unwrap,
                        pluginMatch -> switch (pluginMatch) {
                            case ModSidedPluginMatch sided -> DataResult.success(Either.left(sided));
                            case IdPluginMatch mod -> DataResult.success(Either.right(mod));
                            default -> DataResult.error(() -> "uh oh");
                        }
                );

        boolean matches(Identifier pluginId, PluginSide side);
    }

    private record ModSidedPluginMatch(String modid, PluginSide side) implements PluginMatch {
        public static final Codec<ModSidedPluginMatch> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("mod").forGetter(ModSidedPluginMatch::modid),
                PluginSide.CODEC.fieldOf("side").forGetter(ModSidedPluginMatch::side)
        ).apply(instance, ModSidedPluginMatch::new));

        @Override
        public boolean matches(Identifier pluginId, PluginSide side) {
            return this.modid.equals(pluginId.getNamespace()) && this.side == side;
        }
    }

    private record IdPluginMatch(String id) implements PluginMatch {
        public static final Codec<IdPluginMatch> CODEC = Codec.STRING.xmap(IdPluginMatch::new, IdPluginMatch::id);

        @Override
        public boolean matches(Identifier pluginId, PluginSide side) {
            if (id.contains(":")) {
                return id.equals(pluginId.toString());
            }
            else {
                return id.equals(pluginId.getNamespace());
            }
        }
    }
}
