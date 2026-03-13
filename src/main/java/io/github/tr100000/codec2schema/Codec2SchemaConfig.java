package io.github.tr100000.codec2schema;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Codec2SchemaConfig {
    private Codec2SchemaConfig() {}

    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("codec2schema.json");

    public static boolean defaultDebugMode = false;
    public static boolean defaultAllowInline = true;
    public static boolean inlineSingularReference = false;

    public static void load() {
        try {
            if (Files.notExists(PATH)) {
                save();
                return;
            }

            JsonObject json = Codec2Schema.GSON.fromJson(Files.readString(PATH), JsonObject.class);

            defaultDebugMode = GsonHelper.getAsBoolean(json, "defaultDebugMode", false);
            defaultAllowInline = GsonHelper.getAsBoolean(json, "defaultAllowInline", true);
            inlineSingularReference = GsonHelper.getAsBoolean(json, "inlineSingularReference", false);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load Codec2SChema config!", e);
        }
    }

    public static void save() {
        try {
            Files.deleteIfExists(PATH);

            JsonObject json = new JsonObject();
            json.addProperty("defaultDebugMode", defaultDebugMode);
            json.addProperty("defaultAllowInline", defaultAllowInline);
            json.addProperty("inlineSingularReference", inlineSingularReference);

            Files.writeString(PATH, Codec2Schema.GSON.toJson(json));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load Codec2SChema config!", e);
        }
    }
}
