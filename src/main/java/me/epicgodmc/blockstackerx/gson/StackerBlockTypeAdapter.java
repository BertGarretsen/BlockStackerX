package me.epicgodmc.blockstackerx.gson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import me.epicgodmc.blockstackerx.stacker.StackerPermission;
import me.epicgodmc.blockstackerx.util.StackerLocation;
import org.mineacademy.fo.remain.CompMaterial;

import java.lang.reflect.Type;
import java.util.List;
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
            StackerLocation location = StackerLocation.deserialize(jsonObject.get("location"));
            CompMaterial stackMaterial = CompMaterial.fromString(jsonObject.get("material").getAsString());
            List<StackerPermission> activeperms = context.deserialize(jsonObject.get("activePerms"), new TypeToken<List<StackerPermission>>(){}.getType());
            int value = jsonObject.get("value").getAsInt();

            StackerBlock block = new StackerBlock(owner, identifier, location, stackMaterial, activeperms, value);
            if (block.getSettings() != null)
            {
                return block;
            }
        }
        return null;
    }

    @Override
    public JsonElement serialize(StackerBlock src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("owner", src.getOwner().toString());
        jsonObject.addProperty("identifier", src.getIdentifier());
        jsonObject.add("location", GlobalGson.GSON.toJsonTree(src.getLocation()));
        jsonObject.add("activePerms", GlobalGson.GSON.toJsonTree(src.getActivePerms()));
        jsonObject.addProperty("material", src.getStackMaterial().toString());
        jsonObject.addProperty("value", src.getValue());

        return jsonObject;
    }
}
