package me.epicgodmc.blockstackerx.gson;

import com.google.gson.*;
import me.epicgodmc.blockstackerx.StackerBlock;
import me.epicgodmc.blockstackerx.util.SimpleLocation;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by Bert on 14 Aug 2021
 * Copyright © EpicGodMC
 */
public class StackerBlockTypeAdapter implements JsonSerializer<StackerBlock>, JsonDeserializer<StackerBlock>
{


    @Override
    public StackerBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject())
        {
            JsonObject jsonObject = json.getAsJsonObject();

            UUID owner = UUID.fromString(jsonObject.get("owner").getAsString());
            String identifier = jsonObject.get("identifier").getAsString();
            SimpleLocation location = SimpleLocation.deserialize(jsonObject.get("location"));
            int value = jsonObject.get("value").getAsInt();

            return new StackerBlock(owner, identifier, location, value);
        }
        return null;
    }

    @Override
    public JsonElement serialize(StackerBlock src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("owner", src.getOwner().toString());
        jsonObject.addProperty("identifier", src.getIdentifier());
        jsonObject.add("location", GlobalGson.GSON.toJsonTree(src.getSimpleLocation()));
        jsonObject.addProperty("value", src.getValue());

        return jsonObject;
    }
}
