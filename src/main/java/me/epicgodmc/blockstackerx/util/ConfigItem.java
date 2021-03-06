package me.epicgodmc.blockstackerx.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ConfigItem {


    private CompMaterial material;
    private String name;
    private List<String> lore;
    private Integer amount;
    private Boolean enchanted;

    private Short metadata;

    private Map<String, Object> placeholders = new HashMap<>();
    private Map<String, String> tags = new HashMap<>();

    public ConfigItem(SerializedMap map) {
        this.material = map.getMaterial("Material", CompMaterial.DIRT);
        this.name = map.getString("Name", null);
        this.lore = map.getStringList("Lore", null);
        this.amount = map.getInteger("Amount", 1);
        this.enchanted = map.getBoolean("Glow", null);
        this.metadata = map.getInteger("Metadata", 0).shortValue();
    }

    public static ConfigItem of(ItemStack itemStack) {
        ConfigItem configItem = new ConfigItem();

        configItem.material = CompMaterial.fromItem(itemStack);
        configItem.amount = itemStack.getAmount();
        configItem.metadata = itemStack.getDurability();


        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();

            configItem.name = meta.hasDisplayName() ? meta.getDisplayName() : null;
            configItem.lore = meta.hasLore() ? meta.getLore() : null;
            configItem.enchanted = meta.hasEnchants() && meta.hasEnchant(Enchantment.DURABILITY) && meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);
        }

        return configItem;
    }


    public void setPlaceholders(Map<String, Object> placeholders)
    {
        this.placeholders.putAll(placeholders);
    }


    public void setTag(String key, String value) {
        this.tags.put(key, value);
    }

    public void removeTag(String key) {
        this.tags.remove(key);
    }

    public void setPlaceholder(String key, Object value) {
        this.placeholders.put(key, value);
    }

    public void removePlaceholder(String key) {
        this.placeholders.remove(key);
    }

    public ItemStack buildAndClear() {
        ItemStack result = build();
        clean();
        return result;
    }

    public ItemStack build() {
        ItemCreator.ItemCreatorBuilder creator;
        creator = ItemCreator.of(material);

        String rname = name;
        List<String> rlore = new ArrayList<>(lore);

        if (!placeholders.isEmpty()) {
            for (String var : placeholders.keySet()) {
                String variable = String.format("{%s}", var);
                rname = rname.replace(variable, placeholders.get(var).toString());
                rlore = rlore.stream().map(s -> s.replace(variable, placeholders.get(var).toString())).collect(Collectors.toList());
            }
        }
        if (!tags.isEmpty()) {
            creator.tags(this.tags);
        }

        if (rname != null) creator.name(Common.colorize(rname));
        if (rlore != null) creator.lores(Common.colorize(rlore));
        if (amount != null) creator.amount(amount);
        if (enchanted != null) creator.glow(enchanted);
        if (metadata != null) creator.damage(metadata);

        return creator.build().make();
    }

    public SerializedMap serialize() {
        SerializedMap map = new SerializedMap();

        map.putIfExist("Material", material);
        map.putIfExist("Name", name);
        map.putIfExist("Lore", lore);
        map.putIfExist("Amount", amount);
        map.putIfExist("Glow", enchanted);
        map.putIfExist("Metadata", metadata);

        return map;
    }

    public void clean() {
        this.amount = 1;
        this.placeholders.clear();
        this.tags.clear();
    }
}
