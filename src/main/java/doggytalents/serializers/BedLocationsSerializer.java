package doggytalents.serializers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import doggytalents.DoggyTalents;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;

import static net.minecraftforge.common.DimensionManager.isDimensionRegistered;

public class BedLocationsSerializer implements DataSerializer<Map<DimensionType, Optional<BlockPos>>> {

    @Override
    public void write(PacketBuffer buf, Map<DimensionType, Optional<BlockPos>> value) {
        buf.writeInt(value.size());
        value.forEach((key, value1) -> {
            if (value1.isPresent()){
                buf.writeVarInt(key.getId());
                buf.writeBlockPos(value1.get());
            }
        });
    }

    @Override
    public Map<DimensionType, Optional<BlockPos>> read(PacketBuffer buf) throws IOException {
        Map<DimensionType, Optional<BlockPos>> map = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            int loc = buf.readVarInt();
            if (isDimensionRegistered(loc)) {
                map.put(DimensionType.getById(loc), Optional.of(buf.readBlockPos()));
            } else {
                DoggyTalents.LOGGER.warn("Failed loading from PacketBuffer. Could not find dimension {}", loc);
                map.put(DimensionType.getById(loc), Optional.absent());
            }
        }
        DoggyTalents.LOGGER.debug("Loaded {}", map);

        return map;
    }

    @Override
    public DataParameter<Map<DimensionType, Optional<BlockPos>>> createKey(int id) {
        return new DataParameter<>(id, this);
    }

    @Override
    public Map<DimensionType, Optional<BlockPos>> copyValue(Map<DimensionType, Optional<BlockPos>> value) {
        Map<DimensionType, Optional<BlockPos>> copy = new HashMap<>();
        for(Map.Entry<DimensionType, Optional<BlockPos>> entry : value.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

}
