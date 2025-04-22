package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import doggytalents.lib.ConfigValues;
import net.minecraft.nbt.NBTTagCompound;

public class RoaringGaleTalent extends Talent {

    @Override
    public void onClassCreation(IDogEntity dog) {
        dog.putObject("roarcooldown", 0);
    }

    @Override
    public void writeAdditional(IDogEntity dog, NBTTagCompound tagCompound) {
        int roarCooldown = dog.getObject("roarcooldown", Integer.TYPE);
        tagCompound.setInteger("roarcooldown", roarCooldown);
    }

    @Override
    public void readAdditional(IDogEntity dog, NBTTagCompound tagCompound) {
        dog.putObject("roarcooldown", tagCompound.getInteger("roarcooldown"));
    }

    @Override
    public void livingTick(IDogEntity dog) {
        int roarCooldown = dog.getObject("roarcooldown", Integer.TYPE);

        if(roarCooldown > 0 ) {
            roarCooldown--;
            dog.putObject("roarcooldown", roarCooldown);
            return;
        }
    }

    public int roarCost(IDogEntity dog) {
        byte byte0 = (byte)ConfigValues.TALENT_ROAR_HUNGER_COST;

        if (dog.getTalentFeature().getLevel(this) == 5)
            byte0 = (byte)ConfigValues.TALENT_ROAR_HUNGER_COST_5;

        return byte0;
    }

    public static int getRoarCost(IDogEntity dog) {
        RoaringGaleTalent talent = new RoaringGaleTalent();
        return talent.roarCost(dog);
    }
}
