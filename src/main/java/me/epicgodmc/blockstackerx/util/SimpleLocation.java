package me.epicgodmc.blockstackerx.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.NumberConversions;

import java.util.Objects;

public class SimpleLocation {

    private String world;
    private double x;
    private double y;
    private double z;

    private transient Location location = null;

    public SimpleLocation(final String worldName, final double x, final double y, final double z) {
        this.world = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SimpleLocation(Location loc) {
        setLocation(loc, true);
    }

    public SimpleLocation(Location loc, boolean block) {
        setLocation(loc, block);
    }

    public static SimpleLocation deserialize(JsonElement element) {
        Preconditions.checkArgument(element.isJsonObject());
        JsonObject object = element.getAsJsonObject();

        Preconditions.checkArgument(object.has("x"));
        Preconditions.checkArgument(object.has("y"));
        Preconditions.checkArgument(object.has("z"));
        Preconditions.checkArgument(object.has("world"));

        double x = object.get("x").getAsDouble();
        double y = object.get("y").getAsDouble();
        double z = object.get("z").getAsDouble();
        String world = object.get("world").getAsString();

        return of(world, x, y, z);
    }

    public static SimpleLocation of(String world, double x, double y, double z) {
        Objects.requireNonNull(world, "world");
        return new SimpleLocation(world, x, y, z);
    }

    public static SimpleLocation of(Location location) {
        return of(location, false);
    }

    public static SimpleLocation of(Block block) {
        return of(block.getLocation());
    }

    public static SimpleLocation of(Location location, boolean block) {
        return new SimpleLocation(location, block);
    }

    // change the Location
    private void setLocation(Location loc, boolean block) {
        this.location = loc;
        this.world = loc.getWorld().getName();
        if (block) {
            this.x = loc.getBlockX();
            this.y = loc.getBlockY();
            this.z = loc.getBlockZ();
        } else {
            this.x = loc.getX();
            this.y = loc.getY();
            this.z = loc.getZ();
        }
    }


    // This initializes the Location
    private void initLocation() {
        // if location is already initialized, simply return
        if (location != null) {
            return;
        }

        // get World; hopefully it's initialized at this point
        World world = Bukkit.getWorld(this.world);
        if (world == null) {
            return;
        }

        // store the Location for future calls, and pass it on
        location = new Location(world, x, y, z);
    }


    // This returns the actual Location
    public final Location getLocation() {
        // make sure Location is initialized before returning it
        initLocation();
        return location;
    }

    public SimpleLocation subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public SimpleLocation add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public final String getWorld() {
        return world;
    }

    public final double getX() {
        return x;
    }

    public final int getBlockX() {
        return (int) x;
    }

    public final double getY() {
        return y;
    }

    public final int getBlockY() {
        return (int) y;
    }

    public final double getZ() {
        return z;
    }

    public final int getBlockZ() {
        return (int) z;
    }

    public SimpleLocation getRelative(BlockFace face, int distance) {
        Objects.requireNonNull(face, "face");
        return SimpleLocation.of(this.world, this.x + face.getModX() * distance, this.y + face.getModY() * distance, this.z + face.getModZ() * distance);
    }

    public double length() {
        return Math.sqrt(NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z));
    }

    public double lengthSquared() {
        return NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z);
    }

    public double distance(SimpleLocation o) {
        return Math.sqrt(this.distanceSquared(o));
    }

    public double distanceSquared(SimpleLocation o) {
        if (o == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null location");
        } else if (o.getWorld() != null && this.getWorld() != null) {
            if (!o.getWorld().equals(this.getWorld())) {
                throw new IllegalArgumentException("Cannot measure distance between " + this.getWorld() + " and " + o.getWorld());
            } else {
                return NumberConversions.square(this.x - o.x) + NumberConversions.square(this.y - o.y) + NumberConversions.square(this.z - o.z);
            }
        } else {
            throw new IllegalArgumentException("Cannot measure distance to a null world");
        }
    }

    public Block getBukkitBlock(World world) {
        return world.getBlockAt(getBlockX(), getBlockY(), getBlockZ());
    }


    @Override
    public SimpleLocation clone() {
        try {
            return (SimpleLocation) super.clone();
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

        if (!(o instanceof SimpleLocation)) {
            return false;
        }

        final SimpleLocation other = (SimpleLocation) o;
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
