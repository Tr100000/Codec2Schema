package io.github.tr100000.codec2schema;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum PluginSide implements StringRepresentable {
    MAIN("main"),
    CLIENT("client");

    public static final Codec<PluginSide> CODEC = StringRepresentable.fromEnum(PluginSide::values);

    public final String name;

    PluginSide(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
