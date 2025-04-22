package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.util.WorldUtil;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;

public class EntityAIMoveToBlockGoal extends EntityAIBase {
    protected final EntityDog dog;

    public EntityAIMoveToBlockGoal(EntityDog dogIn) {
        this.dog = dogIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        return this.dog.getTargetBlock() != null && !this.dog.isSitting();
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.dog.getDistanceSq(this.dog.getTargetBlock()) < 0.5) {
            return false;
        }
        return this.dog.hasPath();
    }

    @Override
    public void resetTask() {
        BlockPos target = this.dog.getTargetBlock();

        TileEntityDogBed dogBedTileEntity = WorldUtil.getTileEntity(dog.world, target, TileEntityDogBed.class);

        if (dogBedTileEntity != null) {
            // Double check the bed still has no owner
            if (dogBedTileEntity.getOwnerUUID() == null) {
                dogBedTileEntity.setOwner(this.dog);
                this.dog.setBedPos(DimensionType.getById(this.dog.dimension), target);
            }
        }

        this.dog.setTargetBlock(null);
        this.dog.getAISit().setSitting(true);

        this.dog.world.setEntityState(dog,(byte) 7);
    }

    @Override
    public void startExecuting() {
        BlockPos target = this.dog.getTargetBlock();
        this.dog.getNavigator().tryMoveToXYZ((target.getX()) + 0.5D, target.getY() + 1, (target.getZ()) + 0.5D, 1.0D);
    }
}
