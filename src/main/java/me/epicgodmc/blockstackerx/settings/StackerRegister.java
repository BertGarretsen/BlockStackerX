package me.epicgodmc.blockstackerx.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.epicgodmc.blockstackerx.util.ConfigItem;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StackerRegister {


    @Getter
    public static ConfigItem DEFAULT_ITEM;

    @Getter
    public static ConfigItem USED_DEFAULT_ITEM;

    @Getter
    private static final StackerRegister instance = new StackerRegister();

    @Getter
    @Setter
    private static List<StackerSettings> loadedSettings = new ArrayList<>();


    public void registerSettings(final StackerSettings stackerSettings) {
        loadedSettings.add(stackerSettings);
        stackerSettings.save();
    }

    public void unregisterSettings(final String id)
    {
        for (int i = 0; i < loadedSettings.size(); i++) {
            StackerSettings current = loadedSettings.get(i);
            if (current.getIdentifier().equalsIgnoreCase(id))
            {
                loadedSettings.remove(i);
                break;
            }
        }
    }

    public List<StackerSettings> getLoadedSettingsSorted()
    {
        List<StackerSettings> sorted = new ArrayList<>(loadedSettings);
        Collections.sort(sorted);
        return sorted;
    }

    public void deleteSettings(final String id)
    {
        StackerSettings settings = getSettings(id);
        unregisterSettings(id);
        settings.delete();
    }


    public void deleteSettings(final StackerSettings stackerSettings)
    {
        unregisterSettings(stackerSettings.getIdentifier());
        stackerSettings.delete();
    }

    public StackerSettings getSettings(final String identifier) {
        return loadedSettings.stream().filter(e -> e.getIdentifier().equalsIgnoreCase(identifier)).findAny().orElse(null);
    }

    public boolean settingsExist(final String identifier) {
        return getSettings(identifier) != null;
    }

    public List<String> getLoadedStackerNames() {
        return Common.convert(loadedSettings, StackerSettings::getIdentifier);
    }

    public void loadSettingFiles() {
        loadedSettings.clear();

        for (final File file : FileUtil.getFiles("stackers", "yml")) {
            final StackerSettings stackerSettings = new StackerSettings(file.getName().replace(".yml", ""));
            loadedSettings.add(stackerSettings);
        }

    }


    static {

        //create default stacker itemstack
        DEFAULT_ITEM = ConfigItem.of(ItemCreator
                .of(CompMaterial.IRON_BLOCK)
                .name("&bExample Stacker")
                .lores(Arrays.asList("&1Example Line 1", "&2Example line 2", "&3Example line 3"))
                .glow(true)
                .build().makeSurvival());
        //create default used stacker itemstack
        USED_DEFAULT_ITEM = ConfigItem.of(ItemCreator
                .of(CompMaterial.IRON_BLOCK)
                .name("&b&lExample Stacker")
                .lores(Arrays.asList("&6&l-----------------", "", "&6This Stacker contains {value} blocks", "&6BlockType: {type}", "", "&6&l-----------------"))
                .glow(true)
                .build().makeSurvival());
    }
}
