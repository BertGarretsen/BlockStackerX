package me.epicgodmc.blockstackerx.settings.common;

import lombok.Getter;
import me.epicgodmc.blockstackerx.util.InventoryItem;
import org.mineacademy.fo.collection.SerializedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bert on 17 Sep 2021
 * Copyright © EpicGodMC
 */
public class InventorySettings {

    @Getter
    private String title;
    @Getter
    private int rows;
    @Getter
    private Map<String, InventoryItem> buttons;

    public InventorySettings(SerializedMap map) {
        this.title = map.getString("Title", "Title not found");
        this.rows = map.getInteger("Rows", 3);

        SerializedMap buttons = map.getMap("Buttons");
        this.buttons = new HashMap<>();

        for (String buttonId : buttons.keySet()) {
            InventoryItem item = new InventoryItem(buttons.getMap(buttonId));
            this.buttons.put(buttonId, item);
        }

    }

    public InventorySettings(String title, int rows, Map<String, InventoryItem> buttons) {
        this.title = title;
        this.rows = rows;
        this.buttons = buttons;
    }



    public InventoryItem getButton(String id) {
        return buttons.get(id);
    }

    public int getSlots() {
        return rows * 9;
    }

}
