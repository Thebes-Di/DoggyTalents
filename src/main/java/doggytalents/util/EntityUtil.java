package doggytalents.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class EntityUtil {
    public static class Sorter implements Comparator<Entity> {

        private final Vec3d vec3d;

        public Sorter(Entity entityIn) {
            this.vec3d = entityIn.getPositionVector();
        }

        public Sorter(Vec3d vec3d) {
            this.vec3d = vec3d;
        }

        @Override
        public int compare(Entity entity1, Entity entity2) {
            double d0 = this.vec3d.squareDistanceTo(entity1.getPositionVector());
            double d1 = this.vec3d.squareDistanceTo(entity2.getPositionVector());

            return Double.compare(d0, d1);
        }
    }

}
