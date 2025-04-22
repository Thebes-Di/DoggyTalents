package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import doggytalents.lib.ConfigValues;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ProPercivalalb
 */
public class RescueDogTalent extends Talent {

    @Override
    public void livingTick(IDogEntity dog) {
        EntityPlayer player = (EntityPlayer)dog.getOwner();

        int level = dog.getTalentFeature().getLevel(this);

        //TODO add particles and check how far away dog is
        if (player != null && player.getHealth() <= 6 && level != 0 && dog.getHungerFeature().getDogHunger() > this.healCost(dog)) {
            player.heal((float) (level * ConfigValues.TALENT_RESCUE_HEAL_BASE));
            dog.getHungerFeature().setDogHunger(dog.getHungerFeature().getDogHunger() - this.healCost(dog));
        }
    }

    public int healCost(IDogEntity dog) {
        byte byte0 = (byte)ConfigValues.TALENT_RESCUE_HUNGER_COST;

        if (dog.getTalentFeature().getLevel(this) == 5)
            byte0 = (byte)ConfigValues.TALENT_RESCUE_HUNGER_COST_5;

        return byte0;
    }
}
