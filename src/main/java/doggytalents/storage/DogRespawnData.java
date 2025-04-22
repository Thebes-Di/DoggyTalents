package doggytalents.storage;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import doggytalents.api.feature.EnumMode;
import doggytalents.entity.EntityDog;
import doggytalents.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

public class DogRespawnData  {

    private final DogRespawnStorage storage;
    private final UUID uuid;
    private NBTTagCompound data;

    //TODO Make it list you can only add too
    private static final List<String> TAGS_TO_REMOVE = Lists.newArrayList(
            "Pos", "Health", "Motion", "Rotation", "FallDistance", "Fire", "Air", "OnGround",
            "Dimension", "PortalCooldown", "Passengers", "Leash", "InLove", "Leash", "HurtTime",
            "HurtByTimestamp", "DeathTime", "AbsorptionAmount", "FallFlying", "Brain", "Sitting"); // Remove dog mode

    protected DogRespawnData(DogRespawnStorage storageIn, UUID uuid) {
        this.storage = storageIn;
        this.uuid = uuid;
    }

    public UUID getDogId() {
        return this.uuid;
    }

    public String getDogName() {
        ITextComponent name = NBTUtil.getTextComponent(this.data, "CustomName");
        return name == null ? "" : name.getUnformattedText();
    }

    public UUID getOwnerId() {
        String str = data.getString("OwnerUUID");
        return "".equals(str) ? null : UUID.fromString(str);
    }

    public String getOwnerName() {
        ITextComponent name = NBTUtil.getTextComponent(this.data, "lastKnownOwnerName");
        return name == null ? "" : name.getUnformattedText();
    }

    public void populate(EntityDog dogIn) {
        this.data = new NBTTagCompound();
        dogIn.writeToNBT(this.data);

        // Remove tags that don't need to be saved
        for (String tag : TAGS_TO_REMOVE) {
            this.data.removeTag(tag);
        }

        this.data.removeTag("UUID");
        this.data.removeTag("LoveCause");
    }

    @Nullable
    public EntityDog respawn(World worldIn, EntityPlayer playerIn, BlockPos pos) {
        EntityDog dog = new EntityDog(worldIn);

        // Failed for some reason
        if (pos == null || playerIn == null || worldIn == null) {
            playerIn.sendMessage(new TextComponentTranslation("entity.doggytalents.dog.respawn_failed"));
            return null;
        }
        dog.setBedPos(DimensionType.getById(worldIn.provider.getDimension()), pos);
        dog.setOwnerId(playerIn.getUniqueID());

        NBTTagCompound compoundnbt = dog.writeToNBT(new NBTTagCompound());
        UUID uuid = dog.getUniqueID();
        compoundnbt.merge(this.data);
        dog.setUniqueId(uuid);
        dog.readFromNBT(compoundnbt);
        dog.moveToBlockPosAndAngles(pos, dog.rotationYaw, dog.rotationPitch);

        dog.setMode(EnumMode.DOCILE);
        dog.getAISit().setSitting(false);
        worldIn.spawnEntity(dog);

        return dog;
    }

    public void read(NBTTagCompound compound) {
        this.data = compound.getCompoundTag("data");
    }

    public NBTTagCompound write(NBTTagCompound compound) {
        compound.setTag("data", this.data);
        return compound;
    }
}
