package me.epicgodmc.blockstackerx;

import lombok.Getter;
import me.epicgodmc.blockstackerx.command.blockstacker.BlockStackerCmdGroup;
import me.epicgodmc.blockstackerx.listeners.BlockInteractListener;
import me.epicgodmc.blockstackerx.listeners.BlockModifyListener;
import me.epicgodmc.blockstackerx.listeners.BlockPlaceListener;
import me.epicgodmc.blockstackerx.listeners.PlayerJoinListener;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.Settings;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.WorthSettings;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import me.epicgodmc.blockstackerx.storage.IslandStorage;
import me.epicgodmc.blockstackerx.storage.IslandStorageType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public final class StackerPlugin extends SimplePlugin {


    @Getter
    private static StackerPlugin instance;
    private boolean shutdown = false;
    @Getter private IslandStorage islandStorage;

    @Getter
    private HookManager hookManager;


    @Override
    protected void onPluginStart() {
        Common.ADD_LOG_PREFIX = true;
        Common.ADD_TELL_PREFIX = true;
        Common.setLogPrefix("[BlockStackerX]");

        long time = System.currentTimeMillis();
        instance = this;
        Common.logNoPrefix(Common.configLine());
        Common.logNoPrefix(getLogo());
        this.hookManager = new HookManager(this);
        if (this.hookManager.verify()) {
            this.islandStorage = IslandStorageType.valueOf(Settings.STORAGE_TYPE).getStorage();
            Common.logNoPrefix("Storage type has been set to: " + Settings.STORAGE_TYPE);

            registerCommands();
            Common.logNoPrefix("Registered Commands");

            registerListeners();
            Common.logNoPrefix("Registered listeners");

            StackerRegister.getInstance().loadSettingFiles();
            new WorthSettings();
            Common.logNoPrefix("Loaded files");


            Common.logNoPrefix(String.format("Loaded BlockStackerX in %s MS", System.currentTimeMillis() - time));
        } else {
            Common.log("Could not load BlockStackerX, Missing dependencies: " + StringUtils.join(this.hookManager.getMissingDependencies(), ", "));
            shutdown(1);
        }
        Common.logNoPrefix(Common.configLine());
    }

    @Override
    protected void onPluginStop() {
        if (shutdown) return;

        this.islandStorage.saveAll();
    }

    @Override
    protected void onPluginReload() {
        IslandCache.saveAndUnloadAll();

        registerCommands();
        StackerRegister.getInstance().loadSettingFiles();
    }

    private void registerListeners() {
        registerEvents(new BlockPlaceListener(this));
        registerEvents(new BlockInteractListener(this));
        registerEvents(new PlayerJoinListener());
        registerEvents(new BlockModifyListener());
    }

    private void registerCommands() {
        registerCommands("blockstackerx|bs", new BlockStackerCmdGroup());
    }


    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Localization.class, Settings.class);
    }

    private String getLogo() {
        List<String> logoLines = Arrays.asList(
                "  _____  _            _     _____ _             _          __   __",
                " |  _ \\| |          | |   / ____| |           | |         \\ \\ / /",
                " | |_) | | ___   ___| | _| (___ | |_ __ _  ___| | _____ _ _\\ V / ",
                " |  _ <| |/ _ \\ / __| |/ /\\___ \\| __/ _` |/ __| |/ / _ | '__> <  ",
                " | |_) | | (_) | (__|   < ____) | || (_| | (__|   |  __| | / . \\ ",
                " |____/|_|\\___/ \\___|_|\\_|_____/ \\__\\__,_|\\___|_|\\_\\___|_|/_/ \\_\\"
        );

        return Common.join(logoLines, "\n");
    }


    private void shutdown(int exit) {
        Common.runLater(1, () ->
        {
            if (exit == 0) {
                this.onPluginStop();
            }
            Bukkit.getPluginManager().disablePlugin(this);
        });
    }


    @Override
    public boolean areScriptVariablesEnabled() {
        return false;
    }

    public void registerListener(Listener listener) {
        registerEvents(listener);
    }

    public static void sendDebug(String... lines) {
        if (Settings.DEBUG) {
            Common.log(lines);
        }
    }

}
