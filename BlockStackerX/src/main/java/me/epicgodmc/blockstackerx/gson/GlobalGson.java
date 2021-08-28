package me.epicgodmc.blockstackerx.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.epicgodmc.blockstackerx.StackerBlock;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import org.mineacademy.fo.collection.StrictCollection;

import java.lang.reflect.Modifier;

/**
 * Created by Bert on 14 Aug 2021
 * Copyright © EpicGodMC
 */
public class GlobalGson {

    private static GsonBuilder BUILDER = new GsonBuilder()
            .registerTypeAdapter(IslandCache.class, new IslandCacheTypeAdapter())
            .registerTypeAdapter(StackerBlock.class, new StackerBlockTypeAdapter())
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.hasModifier(Modifier.STATIC) || f.hasModifier(Modifier.TRANSIENT)
                            || (StrictCollection.class.isAssignableFrom(f.getDeclaringClass()) && (f.getName().equalsIgnoreCase("cannotRemoveMessage") || f.getName().equalsIgnoreCase("cannotAddMessage")));
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .disableHtmlEscaping();

    public static Gson GSON;

    static {
        GSON = BUILDER.setPrettyPrinting().create();
    }
}
