package me.epicgodmc.blockstackerx.command.blockstacker;

import me.epicgodmc.blockstackerx.menu.StackerEditMenu;
import me.epicgodmc.blockstackerx.menu.StackerListMenu;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.TabUtil;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;

public class EditorCmd extends SimpleSubCommand {


    //if no arg supplied open inv with all stackers ordered from complete --> draft

    //bs edit [stacker]

    protected EditorCmd(SimpleCommandGroup parent, String sublabel) {
        super(parent, sublabel);

        setDescription("Edit the settings of a specific stacker");
        setUsage("[identifier]");
    }

    @Override
    protected void onCommand() {
        checkConsole();

        if (args.length == 0) {
            new StackerListMenu().displayTo(getPlayer());
        } else if (args.length == 1) {
            String input = args[0];
            if (StackerRegister.getInstance().settingsExist(input)) {
                StackerSettings stackerSettings = StackerRegister.getInstance().getSettings(input);
                if (stackerSettings != null) {
                    new StackerEditMenu(stackerSettings).displayTo(getPlayer());
                }else{
                    Common.tell(getPlayer(), Localization.Command.STACKER_NOT_FOUND);
                }
            }else{
                Common.tell(getPlayer(), Localization.Command.STACKER_NOT_FOUND);
            }
        }else{
            Common.tell(getPlayer(), Localization.Commands.INVALID_ARGUMENT);
        }
    }

    @Override
    protected List<String> tabComplete() {
        final String id = Common.joinRange(1, args);
        return TabUtil.complete(id, StackerRegister.getInstance().getLoadedStackerNames());
    }

}
