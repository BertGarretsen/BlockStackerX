package me.epicgodmc.blockstackerx.settings;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import me.epicgodmc.blockstackerx.util.ConfigItem;
import me.epicgodmc.blockstackerx.util.Offset;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;
import java.util.Map;

@ToString
@AllArgsConstructor
public class StackerSettings extends YamlConfig {



    @Getter
    private String identifier;
    @Getter
    private State state;
    @Getter
    private ConfigItem item;
    @Getter
    private Offset hologramOffset;
    @Getter
    private String valueFormat;
    @Getter
    private int maxStorage;
    @Getter
    private boolean teamStacking;
    @Getter
    private List<CompMaterial> availableBlocks;
    @Getter
    private ConfigItem used;


    public StackerSettings(final String identifier) {
        loadConfiguration("stacker.yml", "stackers/" + identifier + (!identifier.endsWith(".yml") ? ".yml" : ""));
        this.identifier = identifier;

        setHeader("This file is automatically updated", "",
                "Unfortunately, due to the way Bukkit saves all .yml files, it was not possible",
                "to preserve the documentation comments for this file. I apologize", "",
                "If you'd like to view the default file, you can either:",
                "a) Open the plugin Jar with WinRar or similar program",
                "b) View the file on github "/*TODO add link*/);
    }


    @Override
    protected void onLoadFinish() {
        state = getEnum("State", State.class);
        item = new ConfigItem(getMap("Item"));
        String[] splittedHologramOffset = getString("HologramOffset").split(",");
        hologramOffset = new Offset(splittedHologramOffset);

        valueFormat = getString("ValueFormat");
        maxStorage = getInteger("MaxStorage");
        teamStacking = getBoolean("TeamStacking");
        availableBlocks = getMaterialList("AvailableBlocks").getSource();

        used = new ConfigItem(getMap("PickupItem"));
    }


    public CompMaterial getDefaultMaterial() {
        return this.item.getMaterial();
    }


    public ItemStack getNewStacker() {
        return getNewStacker(1);
    }


    public ItemStack getNewStacker(int amount) {
        return getNewStacker(Maps.newHashMap(), amount);
    }

    public ItemStack getNewStacker(Map<String, Object> placeholders, int amount) {
        return getNewStacker(placeholders, Maps.newHashMap(), amount);
    }

    public ItemStack getNewStacker(Map<String, Object> placeholders, Map<String, String> tags, int amount) {
        item.setTags(tags);
        item.setPlaceholders(placeholders);
        item.setAmount(amount);

        item.setTag("StackerIdentifier", this.identifier);
        item.setTag("StackerState", "NEW");
        return item.buildAndClear();
    }

    public void setState(State state) {
        this.state = state;
        save("State", state);
    }

    public void setItem(ConfigItem item) {
        this.item = item;
        save("Item", item.serialize());
    }

    public void setHologramOffset(Offset hologramOffset) {
        this.hologramOffset = hologramOffset;
        save("HologramOffset", hologramOffset.toString());
    }

    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
        save("ValueFormat", valueFormat);
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
        save("MaxStorage", maxStorage);
    }

    public void setTeamStacking(boolean teamStacking) {
        this.teamStacking = teamStacking;
        save("TeamStacking", teamStacking);
    }

    public void setAvailableBlocks(List<CompMaterial> availableBlocks) {
        this.availableBlocks = availableBlocks;
        save("AvailableBlocks", availableBlocks);
    }

    public void setUsed(ConfigItem used) {
        this.used = used;
        save("PickupItem", used.serialize());
    }


    public enum State
    {

        ACTIVE,
        DRAFT

    }



}
