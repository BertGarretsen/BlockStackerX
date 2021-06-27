package me.epicgodmc.blockstackerx;

import lombok.Getter;
import me.epicgodmc.blockstackerx.command.blockstacker.BlockStackerCmdGroup;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public final class StackerPlugin extends SimplePlugin {


    @Getter
    private static StackerPlugin instance;


    @Override
    protected void onPluginStart() {
        long time = System.currentTimeMillis();
        instance = this;
        Common.logNoPrefix(Common.configLine());
        Common.logNoPrefix(getLogo());

        registerCommands("blockstackerx|bs", new BlockStackerCmdGroup());
        StackerRegister.getInstance().loadSettingFiles();


        Common.logNoPrefix(String.format("Loaded BlockStackerX in %s MS", System.currentTimeMillis() - time));
        Common.logNoPrefix(Common.configLine());
    }

    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Localization.class);
    }

    private String getLogo()
    {
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

}
