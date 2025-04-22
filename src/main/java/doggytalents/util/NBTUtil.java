package doggytalents.util;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;
import javax.annotation.Nullable;
import com.google.common.base.Optional;
import java.util.UUID;

public class NBTUtil {

    @Nullable
    public static UUID getUniqueId(NBTTagCompound tag, String key) {
        if (tag.hasUniqueId(key)) {
            return tag.getUniqueId(key);
        } else if (NBTUtil.hasOldUniqueId(tag, key)) {
            return NBTUtil.getOldUniqueId(tag, key);
        }

        return null;
    }

    public static UUID getOldUniqueId(NBTTagCompound Tag, String key) {
        return new UUID(Tag.getLong(key + "Most"), Tag.getLong(key + "Least"));
    }

    public static boolean hasOldUniqueId(NBTTagCompound Tag, String key) {
        return Tag.hasKey(key + "Most", Constants.NBT.TAG_ANY_NUMERIC) && Tag.hasKey(key + "Least", Constants.NBT.TAG_ANY_NUMERIC);
    }

    @Nullable
    public static ITextComponent getTextComponent(NBTTagCompound tag, String key) {
        if (tag.hasKey(key, Constants.NBT.TAG_STRING)) {
            return ITextComponent.Serializer.jsonToComponent(tag.getString(key));
        }
        return null;
    }

    public static void putUniqueId(NBTTagCompound tag, String key, @Nullable UUID uuid) {
        if (uuid != null) {
            tag.setUniqueId(key, uuid);
        }
    }

    public static void putTextComponent(NBTTagCompound tag, String key, @Nullable ITextComponent component) {
        if (component != null) {
            tag.setString(key, ITextComponent.Serializer.componentToJson(component));
        }
    }

    public static void putInt(NBTTagCompound tag, String key, @Nullable Integer value) {
        if (value != null) {
            tag.setInteger(key, value);
        }
    }

    public static void putResourceLocation(NBTTagCompound compound, String key, @Nullable ResourceLocation rl) {
        if (rl != null) {
            compound.setString(key, rl.toString());
        }
    }

    public static void putBlockPos(NBTTagCompound compound, @Nullable BlockPos vec3d) {
        if (vec3d != null) {
            compound.setInteger("x", vec3d.getX());
            compound.setInteger("y", vec3d.getY());
            compound.setInteger("z", vec3d.getZ());
        }
    }

    @Nullable
    public static BlockPos getBlockPos(NBTTagCompound compound) {
        if (compound.hasKey("x", Constants.NBT.TAG_ANY_NUMERIC) && compound.hasKey("y", Constants.NBT.TAG_ANY_NUMERIC) && compound.hasKey("z", Constants.NBT.TAG_ANY_NUMERIC)) {
            return new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
        }

        return null;
    }


    public static void putBlockPos(NBTTagCompound compound, String key, Optional<BlockPos> vec3d) {
        if (vec3d.isPresent()) {
            NBTTagCompound posNBT = new NBTTagCompound();
            putBlockPos(posNBT, vec3d.get());
            compound.setTag(key, posNBT);
        }
    }

    public static Optional<BlockPos> getBlockPos(NBTTagCompound compound, String key) {
        if (compound.hasKey(key, Constants.NBT.TAG_COMPOUND)) {
            return Optional.of(getBlockPos(compound.getCompoundTag(key)));
        }

        return Optional.absent();
    }

}
