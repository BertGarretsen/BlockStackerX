package me.epicgodmc.blockstackerx.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

public class GlobalGson
{

    private static GsonBuilder BUILDER = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.hasModifier(Modifier.STATIC) || f.hasModifier(Modifier.TRANSIENT);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    })
            .disableHtmlEscaping();

    public static Gson GSON;


    //TODO make prettyprint configurable
    static {
        GSON = BUILDER.create();
    }
}
