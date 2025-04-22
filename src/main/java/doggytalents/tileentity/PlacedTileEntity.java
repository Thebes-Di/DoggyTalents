package doggytalents.tileentity;

import java.util.UUID;

import javax.annotation.Nullable;

import doggytalents.util.NBTUtil;
import doggytalents.util.WorldUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class PlacedTileEntity extends TileEntity {

    private @Deprecated @Nullable EntityLiving placer;
    private @Nullable UUID placerUUID;


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.placerUUID = NBTUtil.getUniqueId(compound, "placerId");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTUtil.putUniqueId(compound, "placerId", this.placerUUID);

        return compound;
    }

    public void setPlacer(@Nullable EntityLiving placer) {
        this.placer = placer;
        this.placerUUID = placer == null ? null : placer.getUniqueID();
        this.markDirty();
    }

    @Nullable
    public UUID getPlacerId() {
        return this.placerUUID;
    }

    @Nullable
    public EntityLiving getPlacer() {
        return WorldUtil.getCachedEntity(this.world, EntityLiving.class, this.placer, this.placerUUID);
    }

    // Sync to client
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = this.writeToNBT(new NBTTagCompound());
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }
}
