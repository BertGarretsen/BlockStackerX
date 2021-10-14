package me.epicgodmc.blockstackerx.stacker;

import lombok.Getter;
import lombok.Setter;
import me.epicgodmc.blockstackerx.Global;
import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.hook.hologram.StackerHologram;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import me.epicgodmc.blockstackerx.settings.WorthSettings;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import me.epicgodmc.blockstackerx.util.CustomBlockData;
import me.epicgodmc.blockstackerx.util.StackerLocation;
import me.imspooks.baos.BaosDeserializer;
import me.imspooks.baos.BaosSerializer;
import me.imspooks.baos.io.BaosInputStream;
import me.imspooks.baos.io.BaosOutputStream;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StackerBlock implements BaosSerializer, BaosDeserializer {

    @Getter @Setter private UUID owner;
    @Getter private String identifier;
    @Getter @Setter private StackerLocation location;
    @Setter private CompMaterial stackMaterial;
    @Getter private StackerHologram stackerHologram;
    @Getter private List<StackerPermission> activePerms;

    @Getter private int value;


    @Getter
    private transient StackerSettings settings;

    public StackerBlock() {
    }

    public StackerBlock(UUID owner, String identifier, StackerLocation location) {
        this(owner, identifier, location, null, new ArrayList<>(), 0);
    }

    public StackerBlock(UUID owner, String identifier, StackerLocation location, CompMaterial stackMaterial, int value) {
        this(owner, identifier, location, stackMaterial, new ArrayList<>(), value);
    }

    public StackerBlock(UUID owner, String identifier, StackerLocation location, CompMaterial stackMaterial, List<StackerPermission> activePerms, int value) {
        this.settings = StackerRegister.getInstance().getSettings(identifier);

        this.owner = owner;
        this.identifier = identifier;
        this.location = location;
        this.stackMaterial = stackMaterial;
        this.activePerms = activePerms;
        this.value = value;

        PersistentDataContainer container = new CustomBlockData(location.getBlock(), StackerPlugin.getInstance());
        container.set(Global.STACKER_KEY, PersistentDataType.BYTE, (byte) 0);

        this.stackerHologram = StackerPlugin.getInstance().getHookManager().getNewHologram(settings.getValueFormat());
        this.stackerHologram.create(this.value, settings.getHologramOffset().calc(location));
    }

    public CompMaterial getStackMaterial() {
        if (this.stackMaterial != null) {
            return stackMaterial;
        } else return settings.getDefaultMaterial();
    }

    public void setStackMaterial(CompMaterial material) {
        if (material == null) {
            location.setBlock(settings.getDefaultMaterial().getMaterial());
            return;
        }
        this.stackMaterial = material;
        location.setBlock(material.getMaterial());
    }

    public OfflinePlayer getPlayerOwner() {
        return Remain.getOfflinePlayerByUUID(this.owner);
    }

    public boolean hasStackMaterial() {
        return this.stackMaterial != null;
    }

    public ItemStack getItemForm() {
        return settings.getUsedStacker(this);
    }

    public void delete() {
        unload();
        this.location.setBlock(Material.AIR);
        IslandCache.getCache(StackerPlugin.getInstance().getHookManager().getIslandID(getPlayerOwner())).deleteStacker(location);
    }

    public int getSpaceLeft() {
        int max = settings.getMaxStorage();
        return max - this.value;
    }

    public void subtractValue(int value) {
        this.value -= value;
        checkBlockState();
        updateHologram();
    }

    public void incrementValue(int value) {
        this.value += value;
        updateHologram();
    }

    public boolean canSubtractValue(int value) {
        return (this.value - value) >= 0;
    }

    public void setValue(int value) {
        this.value = value;
        checkBlockState();
        updateHologram();
    }

    public boolean canAddValue(int value) {
        int max = settings.getMaxStorage();
        return (this.value + value) <= max;
    }

    public void updateHologram() {
        if (this.stackerHologram != null) this.stackerHologram.update(this.value);

    }

    public double calculateLevels() {
        if (this.stackMaterial == null) return 0;
        return (getMaterialValue() * this.value);
    }

    public double getMaterialValue() {
        if (this.stackMaterial == null) return 0.0;

        return WorthSettings.getWorth(this.stackMaterial);
    }

    public ItemStack getContainingItemStack(int amount) {
        return this.stackMaterial.toItem(amount);
    }

    public void addPermission(StackerPermission perm) {
        this.activePerms.add(perm);
    }

    public void removePermission(StackerPermission perm) {
        this.activePerms.remove(perm);
    }

    public boolean hasPermission(Player requester, StackerPermission perm) {
        if (hasPermission(requester.getUniqueId(), perm)) return true;
        return this.activePerms.contains(perm);
    }


    public boolean hasPermission(UUID requester, StackerPermission perm) {
        if (requester.equals(this.owner)) return true;
        if (this.activePerms.contains(perm)) return true;
        if (!requester.equals(this.owner) && !this.activePerms.contains(perm) && hasBypass(requester)) {
            OfflinePlayer op = Remain.getOfflinePlayerByUUID(requester);
            if (op != null) {
                if (op.isOnline()) Common.tell(op.getPlayer(), "You just bypassed a permission");
            }
            return true;
        }

        return false;
    }

    public boolean hasBypass(UUID requester) {
        OfflinePlayer player = Remain.getOfflinePlayerByUUID(requester);
        boolean result = false;

        if (player.isOnline()) {
            result = PlayerUtil.hasPerm(player.getPlayer(), "blockstackerx.stacker.bypass") || player.isOp();
        }
        return result;
    }


    public void checkBlockState() {
        if (value <= 0) {
            this.value = 0;
            this.stackMaterial = null;
            setBlockType(settings.getDefaultMaterial());
        }
    }

    private void setBlockType(CompMaterial material) {
        if (material.getMaterial().isBlock()) {
            location.setBlock(material.getMaterial());
        }
    }


    public void unload() {
        stackerHologram.delete();
    }

    @Override
    public void deserialize(BaosInputStream in) throws IOException {
        this.owner = in.readUUID();
        this.identifier = in.readString();
        this.location = new StackerLocation(in);

        String type = in.readString();
        if (!type.equals("NONE")) this.stackMaterial = CompMaterial.valueOf(type);

        this.value = in.readInt();

        int permSize = in.readInt();

        this.activePerms = new ArrayList<>();
        for (int i = 0; i < permSize; i++) {
            StackerPermission perm = StackerPermission.valueOf(in.readString());
            this.activePerms.add(perm);
        }

        this.settings = StackerRegister.getInstance().getSettings(identifier);

        if (this.settings != null) {
            this.stackerHologram = StackerPlugin.getInstance().getHookManager().getNewHologram(settings.getValueFormat());
            this.stackerHologram.create(this.value, settings.getHologramOffset().calc(location));
        }


    }

    public void postLoad() {
        PersistentDataContainer container = new CustomBlockData(this.location.getBlock(), StackerPlugin.getInstance());
        container.set(Global.STACKER_KEY, PersistentDataType.BYTE, (byte) 0);
    }

    @Override
    public void serialize(BaosOutputStream out) throws IOException {
        out.writeUUID(this.owner);
        out.writeString(this.identifier);
        this.location.serialize(out);
        out.writeString(this.stackMaterial == null ? "NONE" : this.stackMaterial.name());
        out.writeInt(this.value);

        out.writeInt(this.activePerms.size());

        for (StackerPermission activePerm : this.activePerms) {
            out.writeString(activePerm.toString());
        }
    }


}
