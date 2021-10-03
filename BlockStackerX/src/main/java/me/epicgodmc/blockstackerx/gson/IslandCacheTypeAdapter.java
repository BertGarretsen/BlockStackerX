package me.epicgodmc.blockstackerx.gson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import me.epicgodmc.blockstackerx.storage.IslandCache;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by Bert on 14 Aug 2021
 * Copyright © EpicGodMC
 */
public class IslandCacheTypeAdapter implements JsonSerializer<IslandCache>, JsonDeserializer<IslandCache> {


    @Override
    public IslandCache deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();

            String UUID = object.get("uuid").getAsString();

            IslandCache islandCache = new IslandCache(UUID);

            Collection<StackerBlock> stackerBlocks = context.deserialize(object.get("stackers"), new TypeToken<Collection<StackerBlock>>() {
            }.getType());
            islandCache.addAllStackers(stackerBlocks);

            return islandCache;
        }
        return null;
    }

    @Override
    public JsonElement serialize(IslandCache src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("uuid", src.getUUID());
        jsonObject.add("stackers", GlobalGson.GSON.toJsonTree(src.getStackers().values()));

        return jsonObject;
    }
}
