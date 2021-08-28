package me.epicgodmc.blockstackerx;

import lombok.Getter;
import lombok.Setter;
import me.epicgodmc.blockstackerx.hook.hologram.StackerHologram;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import me.epicgodmc.blockstackerx.util.SimpleLocation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.UUID;

public class StackerBlock {

    @Getter @Setter private UUID owner;
    @Getter @Setter private final String identifier;
    @Getter @Setter private SimpleLocation simpleLocation;
    @Setter private CompMaterial stackMaterial;
    @Getter private StackerHologram stackerHologram;
    @Getter private int value;


    private final transient StackerSettings settings;

    public StackerBlock(UUID owner, String identifier, SimpleLocation simpleLocation) {
        this(owner, identifier, simpleLocation, 0);
    }

    public StackerBlock(UUID owner, String identifier, SimpleLocation simpleLocation, int value) {
        this.settings = StackerRegister.getInstance().getSettings(identifier);

        this.owner = owner;
        this.identifier = identifier;
        this.simpleLocation = simpleLocation;
        this.value = value;

        this.stackerHologram = StackerPlugin.getInstance().getHookManager().getNewHologram(settings.getValueFormat());
        this.stackerHologram.create(this.value, settings.getHologramOffset().calc(simpleLocation));
    }

    public CompMaterial getStackMaterial() {
        if (this.stackMaterial != null)
        {
            return stackMaterial;
        }else return settings.getDefaultMaterial();
    }

    public ItemStack getItemForm()
    {
        return settings.getUsedStacker(this);
    }

    public void delete()
    {
        unload();
    }


    public void unload()
    {
        stackerHologram.delete();
    }

}
