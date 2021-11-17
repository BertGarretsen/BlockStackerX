package me.epicgodmc.blockstackerx.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public abstract class TargettedCommand extends SimpleSubCommand
{


    protected TargettedCommand(SimpleCommandGroup parent, String sublabel) {
        super(parent, sublabel);
    }

    @Override
    protected void onCommand() {
        final Player player = findPlayer(args[0]);
        onTargettedCommand(player);
    }

    protected abstract void onTargettedCommand(Player target);
}
