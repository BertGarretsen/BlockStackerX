package me.epicgodmc.blockstackerx;

import lombok.Getter;
import lombok.Setter;
import me.epicgodmc.blockstackerx.util.SimpleLocation;

import java.util.UUID;

public class StackerBlock
{

    @Getter @Setter private UUID owner;
    @Getter @Setter private SimpleLocation simpleLocation;

    public StackerBlock(UUID owner, SimpleLocation simpleLocation) {
        this.owner = owner;
        this.simpleLocation = simpleLocation;
    }
}
