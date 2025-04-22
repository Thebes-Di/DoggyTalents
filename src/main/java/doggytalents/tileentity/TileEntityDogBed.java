package doggytalents.tileentity;

import java.util.List;

import doggytalents.DoggyTalents;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.block.DogBedRegistry;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ai.DogLocationManager;
import doggytalents.util.WorldUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import doggytalents.util.NBTUtil;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author ProPercivalalb
 */
public class TileEntityDogBed extends TileEntity implements ITickable {

    private IBedMaterial casingId = IBedMaterial.NULL;
    private IBedMaterial beddingId = IBedMaterial.NULL;

    private @Deprecated @Nullable EntityDog dog;
    private @Nullable UUID dogUUID;

    private @Nullable ITextComponent name;
    private @Nullable ITextComponent ownerName;
    
    public TileEntityDogBed() {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.casingId = DogBedRegistry.CASINGS.get(tag.getString("casingId"));
        this.beddingId = DogBedRegistry.BEDDINGS.get(tag.getString("beddingId"));

        this.dogUUID = NBTUtil.getUniqueId(tag,"ownerId");
        this.name = NBTUtil.getTextComponent(tag,"name");
        this.ownerName = NBTUtil.getTextComponent(tag,"ownerName");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("casingId", this.casingId != null ? this.casingId.getSaveId() : "missing");
        tag.setString("beddingId", this.beddingId != null ? this.beddingId.getSaveId() : "missing");

        NBTUtil.putUniqueId(tag,"ownerId", this.dogUUID);
        NBTUtil.putTextComponent(tag,"name", this.name);
        NBTUtil.putTextComponent(tag,"ownerName", this.ownerName);
        return tag;
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
    }


    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }
    
    @Override
    public void update() {
        List<EntityDog> dogs = this.world.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(this.pos).grow(3, 2, 3));
         
        if (dogs != null && dogs.size() > 0) {
            for (int index = 0; index < dogs.size(); index++) {
                EntityDog dog = (EntityDog)dogs.get(index);
                
                if(dog.getMaxHealth() / 2 >= dog.getHealth()) {
                    //if(dog.bedHealTick <= 0) {
                        dog.heal(0.5F);
                        //dog.bedHealTick = 20 * 20;
                    //}
                    //dog.bedHealTick--;
                }
            }
        }

    }

    public void setCasingId(IBedMaterial newId) {
        this.casingId = newId;
        this.markDirty();
    }
    
    public void setBeddingId(IBedMaterial newId) {
        this.beddingId = newId;
        this.markDirty();
    }
    
    public IBedMaterial getCasingId() {
        return this.casingId;
    }
    
    public IBedMaterial getBeddingId() {
        return this.beddingId;
    }

    public void setOwner(@Nullable EntityDog owner) {
        this.setOwner(owner == null ? null : owner.getUniqueID());
        this.ownerName = owner == null ? null : owner.getDisplayName();

        this.dog = owner;

    }

    public void setOwner(@Nullable UUID owner) {
        this.dog = null;
        this.dogUUID = owner;

        this.markDirty();

        DoggyTalents.LOGGER.debug("Set bed owner to {}", owner);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.dogUUID;
    }

    @Nullable
    public EntityDog getOwner() {
        return WorldUtil.getCachedEntity(this.world, EntityDog.class, this.dog, this.dogUUID);
    }

    @Nullable
    public ITextComponent getBedName() {
        return this.name;
    }

    @Nullable
    public ITextComponent getOwnerName() {
        if (this.dogUUID == null || this.world == null) {
            return null;
        }

        List<DogLocationManager.DogLocation> locList = DogLocationManager
                .getHandler(this.world)
                .getAll(loc -> loc.getEntityId().equals(this.dogUUID));

        if (!locList.isEmpty()) {
            DogLocationManager.DogLocation locData = locList.get(0);
            ITextComponent text = locData.getName(this.world);
            if (text != null) {
                this.ownerName = text;
            }
        }

        return this.ownerName;
    }

    public boolean shouldDisplayName(EntityLiving camera) {
        return true;
    }

    public void setBedName(@Nullable ITextComponent nameIn) {
        this.name = nameIn;
        this.markDirty();
    }

    public void setOwnerName(@Nullable ITextComponent nameIn) {
        this.ownerName = nameIn;
        this.markDirty();
    }
}
