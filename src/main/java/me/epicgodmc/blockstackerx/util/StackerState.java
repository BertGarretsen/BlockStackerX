package me.epicgodmc.blockstackerx.util;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum StackerState
{

    NEW("NEW"),
    USED("USED");

    String tag;

    StackerState(String tag) {
        this.tag = tag;
    }

    public static Optional<StackerState> getByTag(String tag)
    {
        return Arrays.stream(StackerState.values()).filter(e -> e.getTag().equalsIgnoreCase(tag)).findAny();
    }

}
