package me.epicgodmc.blockstackerx.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import me.imspooks.baos.io.BaosInputStream;
import me.imspooks.baos.io.BaosOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.remain.Remain;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Bert on 29 Aug 2021
 * Copyright © EpicGodMC
 */
public class StackerLocation implements Cloneable {

    @Getter
    UUID world;

    @Getter
    int x, y, z;

    public StackerLocation(@NotNull UUID world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public StackerLocation(@NotNull Block block)
    {
        Location l = block.getLocation();

        this.world = l.getWorld().getUID();
        this.x = l.getBlockX();
        this.y = l.getBlockY();
        this.z = l.getBlockZ();
    }


    public StackerLocation(@NotNull Location location) {
        Valid.checkNotNull(location);
        Valid.checkNotNull(location.getWorld());
        this.world = location.getWorld().getUID();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public StackerLocation(BaosInputStream in) throws IOException {
        this.world = in.readUUID();
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
    }

    public static StackerLocation deserialize(JsonElement element) {
        Preconditions.checkArgument(element.isJsonObject());
        JsonObject object = element.getAsJsonObject();

        Preconditions.checkArgument(object.has("x"));
        Preconditions.checkArgument(object.has("y"));
        Preconditions.checkArgument(object.has("z"));
        Preconditions.checkArgument(object.has("world"));

        int x = object.get("x").getAsInt();
        int y = object.get("y").getAsInt();
        int z = object.get("z").getAsInt();
        UUID world = UUID.fromString(object.get("world").getAsString());

        return new StackerLocation(world, x, y ,z);
    }


    public Location toLocation(double x, double y, double z) {
        return new Location(getBukkitWorld(), x + this.x, y + this.y, z + this.z);
    }

    public  void setBlock(Material material)
    {
        Remain.setTypeAndData(getBlock(), material, (byte) 0,  false);
    }

    public Block getBlock()
    {
        return toLocation().getBlock();
    }

    public Location toLocation(Offset offset)
    {
        return toLocation(offset.getX(), offset.getY(), offset.getZ());
    }


    public Location toLocation() {
        return new Location(getBukkitWorld(), x, y, z);
    }

    public World getBukkitWorld() {
        return Bukkit.getWorld(world);
    }


    public void serialize(BaosOutputStream out) throws IOException {
        out.writeUUID(this.world);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
    }

    public static StackerLocation randomLocation()
    {
        return new StackerLocation(UUID.randomUUID(), RandomUtil.nextBetween(0, 100), RandomUtil.nextBetween(0, 100), RandomUtil.nextBetween(0, 100));
    }


    @Override
    public StackerLocation clone() {
        try {
            return (StackerLocation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof StackerLocation)) {
            return false;
        }

        final StackerLocation other = (StackerLocation) o;
        return Double.compare(this.getX(), other.getX()) == 0 &&
                Double.compare(this.getY(), other.getY()) == 0 &&
                Double.compare(this.getZ(), other.getZ()) == 0 &&
                this.getWorld().equals(other.getWorld());
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;

        final long x = Double.doubleToLongBits(this.getX());
        final long y = Double.doubleToLongBits(this.getY());
        final long z = Double.doubleToLongBits(this.getZ());

        result = result * PRIME + (int) (x >>> 32 ^ x);
        result = result * PRIME + (int) (y >>> 32 ^ y);
        result = result * PRIME + (int) (z >>> 32 ^ z);
        result = result * PRIME + this.getWorld().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getWorld() + "," + getX() + "," + getY() + "," + getZ();
    }

}
