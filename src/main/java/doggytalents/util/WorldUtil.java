package doggytalents.util;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import com.google.common.base.Optional;
import java.util.UUID;

public class WorldUtil {

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends TileEntity> T getTileEntity(IBlockAccess worldIn, BlockPos posIn, Class<T> type) {
        TileEntity tileEntity = worldIn.getTileEntity(posIn);
        if (tileEntity.getClass().isAssignableFrom(type)) {
            return (T) tileEntity;
        }

        return null;
    }

    @Nullable
    public static <T extends Entity> T getCachedEntity(@Nullable World worldIn, Class<T> type, @Nullable T cached, @Nullable UUID uuid) {
        if ((cached == null || cached.isDead) && uuid != null && worldIn instanceof WorldServer) {
            Entity entity = ((WorldServer) worldIn).getEntityFromUuid(uuid);
            if (entity != null && entity.getClass().isAssignableFrom(type)) {
                return (T) entity;
            } else {
                return null;
            }
        }

        return cached;
    }

    public static Optional<BlockPos> toImmutable(BlockPos pos) {
        return pos != null ? Optional.of(pos.toImmutable()) : Optional.absent();
    }
}
