package doggytalents.storage;

import static doggytalents.api.lib.Reference.MOD_ID;
import static net.minecraftforge.common.util.Constants.NBT.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import doggytalents.DoggyTalents;
import doggytalents.entity.EntityDog;
import doggytalents.util.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class DogRespawnStorage extends WorldSavedData {

    private Map<UUID, DogRespawnData> respawnDataMap = Maps.newConcurrentMap();
    public static final String STORAGE_DOG_RESPAWN = MOD_ID + "DeadDogs";

    public DogRespawnStorage() {
        super(STORAGE_DOG_RESPAWN);
    }

    public DogRespawnStorage(String name) {
        super(name);
    }

    public static DogRespawnStorage get(World world) {
        if (!(world instanceof WorldServer)) {
            throw new RuntimeException("Tried to access dog respawn data from the client. This should not happen...");
        }

        MapStorage storage = world.getMapStorage();
        DogRespawnStorage instance = (DogRespawnStorage) storage.getOrLoadData(DogRespawnStorage.class, STORAGE_DOG_RESPAWN);
        if (instance == null) {
            instance = new DogRespawnStorage();
            storage.setData(STORAGE_DOG_RESPAWN, instance);
        }
        return instance;
    }

    public Stream<DogRespawnData> getDogs(@Nonnull UUID ownerId) {
        return this.respawnDataMap.values().stream()
                .filter(data -> ownerId.equals(data.getOwnerId()));
    }

    public Stream<DogRespawnData> getDogs(@Nonnull String ownerName) {
        return this.respawnDataMap.values().stream()
                .filter(data -> ownerName.equals(data.getOwnerName()));
    }

    @Nullable
    public DogRespawnData getData(UUID uuid) {
        if (this.respawnDataMap.containsKey(uuid)) {
            return this.respawnDataMap.get(uuid);
        }

        return null;
    }

    @Nullable
    public DogRespawnData remove(UUID uuid) {
        if (this.respawnDataMap.containsKey(uuid)) {
            DogRespawnData storage = this.respawnDataMap.remove(uuid);

            // Mark dirty so changes are saved
            this.markDirty();
            return storage;
        }

        return null;
    }

    @Nullable
    public DogRespawnData putData(EntityDog dogIn) {
        UUID uuid = dogIn.getUniqueID();

        DogRespawnData storage = new DogRespawnData(this, uuid);
        storage.populate(dogIn);

        this.respawnDataMap.put(uuid, storage);
        // Mark dirty so changes are saved
        this.markDirty();
        return storage;
    }

    public Set<UUID> getAllUUID() {
        return Collections.unmodifiableSet(this.respawnDataMap.keySet());
    }

    public Collection<DogRespawnData> getAll() {
        return Collections.unmodifiableCollection(this.respawnDataMap.values());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.respawnDataMap.clear();

        NBTTagList list = nbt.getTagList("respawnData", TAG_COMPOUND);

        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound respawnCompound = list.getCompoundTagAt(i);

            UUID uuid = NBTUtil.getUniqueId(respawnCompound, "uuid");
            DogRespawnData respawnData = new DogRespawnData(this, uuid);
            respawnData.read(respawnCompound);

            if (uuid == null) {
                DoggyTalents.LOGGER.info("Failed to load dog respawn data. Please report to mod author...");
                DoggyTalents.LOGGER.info(respawnData);
                continue;
            }

            this.respawnDataMap.put(uuid, respawnData);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();

        for (Map.Entry<UUID, DogRespawnData> entry : this.respawnDataMap.entrySet()) {
            NBTTagCompound respawnCompound = new NBTTagCompound();

            DogRespawnData respawnData = entry.getValue();
            NBTUtil.putUniqueId(respawnCompound, "uuid", entry.getKey());
            respawnData.write(respawnCompound);

            list.appendTag(respawnCompound);
        }

        compound.setTag("respawnData", list);

        return compound;
    }

}
