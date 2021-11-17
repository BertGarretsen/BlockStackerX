package me.epicgodmc.blockstackerx.command.blockstacker;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class BlockStackerCmdGroup extends SimpleCommandGroup
{

    @Override
    protected void registerSubcommands() {
        registerSubcommand(new CreateCmd(this, "create"));
        registerSubcommand(new GiveCmd(this, "give"));
        registerSubcommand(new EditorCmd(this, "edit"));
        registerSubcommand(new ReloadCommand(this, "reload|rl"));
        registerSubcommand(new DebugCmd(this, "debug"));
    }

    @Override
    protected String getCredits() {
        return "Join https://discord.gg/rBAPTEMSPp for extra support!\n   Type /"+getLabel()+" ? for help!";
    }
}
