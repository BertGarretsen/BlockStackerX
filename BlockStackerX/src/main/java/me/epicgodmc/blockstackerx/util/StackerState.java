package me.epicgodmc.blockstackerx.util;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.nbt.NBTCompound;
import org.mineacademy.fo.remain.nbt.NBTItem;

import java.util.Arrays;
import java.util.Optional;

@Getter
public class StackerState
{

    String id;
    StackerState.State state;

    public StackerState(ItemStack item)
    {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound compound = nbtItem.getCompound("stacker_data");
        if (compound == null) return;

        this.id = compound.getString("StackerIdentifier");
        this.state = State.getByTag(compound.getString("StackerState")).orElse(State.NEW);
    }

    @Getter
    public enum State {

        NEW("NEW"),
        USED("USED");

        String tag;

        State(String tag) {
            this.tag = tag;
        }

        public static Optional<State> getByTag(String tag)
        {
            return Arrays.stream(State.values()).filter(e -> e.getTag().equalsIgnoreCase(tag)).findAny();
        }

    }
}
