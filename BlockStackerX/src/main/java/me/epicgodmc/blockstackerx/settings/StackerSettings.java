package me.epicgodmc.blockstackerx.settings;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import me.epicgodmc.blockstackerx.util.ConfigItem;
import me.epicgodmc.blockstackerx.util.Offset;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public StackerSettings setIdentifier(String identifier) {
        StackerRegister.getInstance().deleteSettings(this);
        StackerSettings clone = StackerSettings.clone(identifier, this);
        StackerRegister.getInstance().registerSettings(clone);
        return clone;
    }

//    public void setIdentifier(String identifier) {
//        this.identifier = identifier;
//
//        try {
//            Path filePath = Paths.get(getFile().getPath());
//            Files.move(filePath, filePath.resolveSibling(identifier + ".yml"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        reload();
//    }

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

    public List<String> getAvailableBlockNames()
    {
        return Common.convert(availableBlocks, value -> ItemUtil.bountifyCapitalized(value.getMaterial()));
    }

    public void setAvailableBlocks(String[] array) {
        List<CompMaterial> converted = Common.convert(array, value -> CompMaterial.fromString(value.trim()));
        setAvailableBlocks(converted);
    }

    public void setAvailableBlocks(List<CompMaterial> availableBlocks) {
        this.availableBlocks = availableBlocks;
        save("AvailableBlocks", availableBlocks);
    }

    public void setUsed(ConfigItem used) {
        this.used = used;
        save("PickupItem", used.serialize());
    }

    public String isTeamStackingFancy() {
        return isTeamStacking() ? "&a&lTrue" : "&4&lFalse";
    }

    public static StackerSettings clone(String identifier, StackerSettings old) {
        StackerSettings stackerSettings = new StackerSettings(identifier);

        stackerSettings.state = old.state;
        stackerSettings.item = old.item;
        stackerSettings.hologramOffset = old.hologramOffset;
        stackerSettings.valueFormat = old.valueFormat;
        stackerSettings.maxStorage = old.maxStorage;
        stackerSettings.teamStacking = old.teamStacking;
        stackerSettings.availableBlocks = old.availableBlocks;
        stackerSettings.used = old.used;

        return stackerSettings;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StackerSettings that = (StackerSettings) o;

        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

    public enum State {

        ACTIVE,
        DRAFT

    }


}
