package me.epicgodmc.blockstackerx;

import lombok.Getter;
import me.epicgodmc.blockstackerx.command.blockstacker.BlockStackerCmdGroup;
import me.epicgodmc.blockstackerx.listeners.BlockBreakListener;
import me.epicgodmc.blockstackerx.listeners.BlockPlaceListener;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.Settings;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public final class StackerPlugin extends SimplePlugin {


    @Getter
    private static StackerPlugin instance;
    private boolean shutdown = false;


    @Getter
    private HookManager hookManager;


    @Override
    protected void onPluginStart() {
        long time = System.currentTimeMillis();
        instance = this;
        Common.logNoPrefix(Common.configLine());
        Common.logNoPrefix(getLogo());
        this.hookManager = new HookManager(this);
        if (this.hookManager.verify()) {


            registerListeners();
            registerCommands("blockstackerx|bs", new BlockStackerCmdGroup());
            StackerRegister.getInstance().loadSettingFiles();


            Common.logNoPrefix(String.format("Loaded BlockStackerX in %s MS", System.currentTimeMillis() - time));
        } else {
            Common.log("Could not load BlockStackerX, Missing dependencies: " + StringUtils.join(this.hookManager.getMissingDependencies(), ", "));
            shutdown(1);
        }
        Common.logNoPrefix(Common.configLine());
    }


    private void registerListeners()
    {
        registerEvents(new BlockPlaceListener(this));
        registerEvents(new BlockBreakListener(this));
    }

    @Override
    protected void onPluginStop() {
        if (shutdown) return;


    }

    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Localization.class);
    }

    private String getLogo() {
        List<String> logoLines = Arrays.asList(
                "   ____  _            _     _____ _             _          __   __",
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
            // 0 = safe stop
            // 1 = force stop

            if (exit == 0) {
                this.onPluginStop();
            }
            Bukkit.getPluginManager().disablePlugin(this);
        });
    }

    public static void sendDebug(String... lines) {
        if (Settings.DEBUG) {
            Common.log(lines);
        }
    }

}
