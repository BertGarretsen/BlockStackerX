package me.epicgodmc.blockstackerx.command.blockstacker;

import me.epicgodmc.blockstackerx.menu.StackerEditMenu;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class CreateCmd extends SimpleSubCommand {

    protected CreateCmd(SimpleCommandGroup parent, String sublabel) {
        super(parent, sublabel);

        setDescription("Create a new stacker type");
        setUsage("<Identifier>");
        setMinArguments(1);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        if (!StackerRegister.getInstance().settingsExist(args[0]))
        {
            StackerSettings stackerSettings = new StackerSettings(args[0]);
            StackerRegister.getInstance().registerSettings(stackerSettings);
            new StackerEditMenu(stackerSettings).displayTo(getPlayer());
        }else{
            Common.tell(getPlayer(), Localization.Command.STACKER_ALREADY_EXISTS.replace("{stacker_type}", args[0]));
        }

    }
}
