package me.epicgodmc.blockstackerx;

import lombok.Getter;
import lombok.Setter;
import me.epicgodmc.blockstackerx.hook.hologram.StackerHologram;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import me.epicgodmc.blockstackerx.util.SimpleLocation;

import java.util.UUID;

public class StackerBlock
{

    @Getter @Setter private UUID owner;
    @Getter @Setter private final String identifier;
    @Getter @Setter private SimpleLocation simpleLocation;
    @Getter private StackerHologram stackerHologram;
    @Getter private int value;

    public StackerBlock(UUID owner, String identifier, SimpleLocation simpleLocation)
    {
        StackerSettings stackerSettings = StackerRegister.getInstance().getSettings(identifier);

        this.owner = owner;
        this.identifier = identifier;
        this.simpleLocation = simpleLocation;
        this.value = 0;


        this.stackerHologram = StackerPlugin.getInstance().getHookManager().getNewHologram(stackerSettings.getValueFormat());
        this.stackerHologram.create(this.value, stackerSettings.getHologramOffset().calc(simpleLocation));
    }
}
