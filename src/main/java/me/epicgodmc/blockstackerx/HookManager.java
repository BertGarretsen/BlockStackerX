package me.epicgodmc.blockstackerx;

import lombok.Getter;
import me.epicgodmc.blockstackerx.hook.hologram.HologramProviderType;
import me.epicgodmc.blockstackerx.hook.hologram.StackerHologram;
import me.epicgodmc.blockstackerx.hook.skyblock.BentoBox_Hook;
import me.epicgodmc.blockstackerx.hook.skyblock.SkyblockHook;
import me.epicgodmc.blockstackerx.settings.Settings;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.managers.AddonsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HookManager {

    private StackerPlugin plugin;


    @Getter private SkyblockHook skyblockHook;
    @Getter private HologramProviderType hologramProviderType;


    public HookManager(StackerPlugin plugin) {
        this.plugin = plugin;
        if (Common.doesPluginExist("BentoBox")) {
            AddonsManager addonsManager = BentoBox.getInstance().getAddonsManager();
            String chosenAddon = "na";
            String[] valid_gamemodes = new String[]{"BSkyBlock", "AOneBlock"};

            for (String valid_gamemode : valid_gamemodes) {
                if (addonsManager.getAddonByName(valid_gamemode).isPresent()) {
                    chosenAddon = valid_gamemode;
                    break;
                }
            }

            if (chosenAddon.equals("na")) {
                Common.log("Could not find a valid BentoBox addon as gamemode. valid addons are: " + Common.join(Arrays.asList(valid_gamemodes), ", "));
                return;
            }

            if (!addonsManager.getAddonByName("Level").isPresent()) {
                Common.log("Could not find the Level addon loaded by BentoBox, please install it before using BlockStackerX");
                return;
            }
            Common.logF("Using BentoBox ( Level & %s Addon) as Game dependency", chosenAddon);
            this.skyblockHook = new BentoBox_Hook(chosenAddon);
        }

        HologramProviderType provider = HologramProviderType.valueOf(Settings.HOLOGRAM_PROVIDER_TYPE.toUpperCase());

        this.hologramProviderType = provider;
        Common.log(this.hologramProviderType.getEnableMessage());

    }

    protected boolean verify() {
        return skyblockHook != null &&
                hologramProviderType != null;
    }

    protected List<String> getMissingDependencies() {
        List<String> output = new ArrayList<>();

        if (skyblockHook == null) output.add("Skyblock");
        if (hologramProviderType == null) output.add("Hologram");


        return output;
    }

    public StackerHologram getNewHologram(String format, StackerBlock block) {
        return this.hologramProviderType.getProvider().get(format, block);
    }


    public String getIslandID(OfflinePlayer player) {
        return this.skyblockHook.getIslandId(player);
    }

    public boolean canModifyStacker(Player player, StackerBlock stacker) {
        return this.skyblockHook.canModifyStacker(stacker, player);
    }
}
