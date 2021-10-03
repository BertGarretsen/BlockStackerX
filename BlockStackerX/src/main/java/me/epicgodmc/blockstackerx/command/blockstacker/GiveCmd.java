package me.epicgodmc.blockstackerx.command.blockstacker;

import me.epicgodmc.blockstackerx.command.TargettedCommand;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.TabUtil;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.settings.SimpleLocalization;

import java.util.List;

public class GiveCmd extends TargettedCommand {

    protected GiveCmd(SimpleCommandGroup parent, String sublabel) {
        super(parent, sublabel);

        setDescription("Spawn a new stacker");
        setUsage("<target> <identifier> [amount]");
        setMinArguments(2);
    }

    @Override
    protected void onTargettedCommand(Player target) {
        StackerSettings settings = StackerRegister.getInstance().getSettings(args[1]);
        if (settings == null) {
            Common.tell(getSender(), Replacer.replaceArray(Localization.Command.STACKER_NOT_FOUND, "stacker_type", args[1], "available_stackers", Common.join(StackerRegister.getInstance().getLoadedStackerNames(), ", ")));
            return;
        }
        int amount = args.length == 3 ? findNumber(2, SimpleLocalization.Commands.INVALID_NUMBER) : 1;
        PlayerUtil.addItems(target.getInventory(), settings.getNewStacker(amount));
        Common.tell(getSender(), Replacer.replaceArray(Localization.Command.STACKER_GIVEN, "target", target.getName(), "amount", amount, "stacker_type", settings.getIdentifier()));
        Common.tell(target, Replacer.replaceArray(Localization.Command.STACKER_RECEIVE, "amount", amount, "stacker_type", settings.getIdentifier()));
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 3) {
            return completeLastWord(1, 2, 5, 10, 32, 64);
        } else if (args.length != 2) {
            return null;
        } else {
            final String id = Common.joinRange(1, args);
            return TabUtil.complete(id, StackerRegister.getInstance().getLoadedStackerNames());
        }
    }


}
