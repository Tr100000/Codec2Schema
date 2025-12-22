package io.github.tr100000.codec2schema.impl.map;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

// Please look away.
public final class MysteryClass {
    private MysteryClass() {}

    public static List<RecordCodecBuilder<?, ?>> something;

    public static void setSomething(App<?, ?>... somethingArray) {
        something = new ObjectArrayList<>(somethingArray.length);
        for (var item : somethingArray) {
            if (item instanceof RecordCodecBuilder<?,?> recordCodecBuilder) {
                something.add(recordCodecBuilder);
            }
        }
    }
}
