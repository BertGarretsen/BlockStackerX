package me.epicgodmc.blockstackerx.command.blockstacker;

import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class CreateCmd extends SimpleSubCommand
{

    protected CreateCmd(SimpleCommandGroup parent, String sublabel) {
        super(parent, sublabel);

        setDescription("Create a new stacker type");

    }

    @Override
    protected void onCommand() {
        checkConsole();
        //new StackerCreateConversation().start(getPlayer());
    }
}
