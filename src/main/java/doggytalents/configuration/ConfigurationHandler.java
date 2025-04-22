package doggytalents.configuration;

import java.util.ArrayList;
import java.util.List;

import doggytalents.lib.ConfigValues;
import doggytalents.lib.TalentNames;
import net.minecraftforge.common.config.Configuration;

/**
 * @author ProPercivalalb, NovaViper
 */
public class ConfigurationHandler {

    public static Configuration CONFIG;
    public static final String CATEGORY_DOGGYSETTINGS = "doggySettings";
    public static final String CATEGORY_TALENT = "talents";
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_ROARING_GALE_SETTING = "roaringSettings";
    public static final String CATEGORY_RESCUE_DOG_SETTING = "rescueSettings";
    
    public static void init(Configuration configuration) {
        CONFIG = configuration;
        loadConfig();
    }
    
     /** TODO NOTE from NovaViper
      *     I did indeed test out these configurations in game and they work perfectly within the world!
      *     I haven't tested tenDayPuupys or isStartingItemsEnabled (though this one will most likely
      *     require a world restart for sure). However, restarting Minecraft (not just the world) might
      *     be required for the immortal configuration.
      *
      *     If you have any questions, you can look at the links in {@link DoggyTalentsGuiFactory}
      *     or contact me again at my email <nova.gamez15+code@gmail.com> or in our conversation on Minecraft
      *     Forums. Thanks!
      */
    public static void loadConfig() {
        CONFIG.addCustomCategoryComment(CATEGORY_GENERAL, "General settings for the mod");
        CONFIG.addCustomCategoryComment(CATEGORY_DOGGYSETTINGS, "Change certain behaviors of dogs");
        CONFIG.addCustomCategoryComment(CATEGORY_TALENT, "Enable and disable talents here as you wish");
        CONFIG.addCustomCategoryComment(CATEGORY_ROARING_GALE_SETTING, "Set roaring gale");
        CONFIG.addCustomCategoryComment(CATEGORY_RESCUE_DOG_SETTING, "Set rescue dog");

        //Creates list for general settings
        List<String> orderDTGeneral = new ArrayList<String>();

        ConfigValues.DEBUG_MODE = CONFIG.get(CATEGORY_GENERAL, "debugMode", false, "Enables debugging mode, which would output values for the sake of finding issues in the mod.").setRequiresMcRestart(false).setRequiresWorldRestart(false).getBoolean(false);

        orderDTGeneral.add("debugMode");

        //Sets the category property order to that of which you have set the list above
        CONFIG.setCategoryPropertyOrder(CATEGORY_GENERAL, orderDTGeneral);

        //Creates list for dog behavior settings
        List<String> orderDSetting = new ArrayList<String>();
         
        ConfigValues.DOGS_IMMORTAL = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isDogImmortal", true, "Determines if dogs die when their health reaches zero. If true, dogs will not die, and will instead become incapacitated.").setRequiresMcRestart(true).getBoolean(true);
        ConfigValues.TIME_TO_MATURE = CONFIG.get(CATEGORY_DOGGYSETTINGS, "timeToMature", 48000, "The time in ticks it takes for a baby dog to become an adult, default 48000 (2 Minecraft days) and minimum 0", 0, Integer.MAX_VALUE).getInt();
        ConfigValues.IS_HUNGER_ON = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isHungerOn", true, "Enables hunger mode for the dog").getBoolean(true);
        //Constants.barkRate = config.get("doggySettings", "barkRate", 10, "Default is 10, higher the number the slower the dogs bark. Lower the number the faster the dogs bark.").getInt(10);
        ConfigValues.DIRE_PARTICLES = CONFIG.get(CATEGORY_DOGGYSETTINGS, "direParticles", true, "Enables the particle effect on Dire Level 30 dogs.").getBoolean(true);
        ConfigValues.STARTING_ITEMS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isStartingItemsEnabled", false, "When enabled you will spawn with a guide, Doggy Charm and Command Emblem.").getBoolean(false);
        ConfigValues.DOG_GENDER = CONFIG.get(CATEGORY_DOGGYSETTINGS, "dogGender", true, "When enabled, dogs will be randomly assigned genders and will only mate and produce children with the opposite gender").setRequiresMcRestart(true).getBoolean(false);
        ConfigValues.DOG_WHINE_WHEN_HUNGER_LOW = CONFIG.get(CATEGORY_DOGGYSETTINGS, "shouldWhineWhenStarving", true, "Determines if dogs should whine when hunger reaches below 20 DP").getBoolean(true);
        ConfigValues.USE_DT_TEXTURES = CONFIG.get(CATEGORY_DOGGYSETTINGS, "useDTTextures", true, "If disabled will use the default minecraft wolf skin for all dog textures.").getBoolean(true);
        ConfigValues.RENDER_CHEST = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggyChest", true, "When enabled, dogs with points in pack puppy will have chests on their side.").getBoolean(true);
        ConfigValues.RENDER_SADDLE = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggySaddle", true, "When enabled, dogs with points in wolf mount will have a saddle on.").getBoolean(true);
        ConfigValues.RENDER_BLOOD = CONFIG.get(CATEGORY_DOGGYSETTINGS, "bloodWhenIncapacitated", true, "When enabled, Dogs will show blood texture while incapacitated.").getBoolean(true);
        ConfigValues.RENDER_WINGS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggyWings", false, "When enabled, Dogs will have wings when at level 5 pillow paw").getBoolean(false);
        ConfigValues.RENDER_ARMOUR = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggyArmour", false, "When enabled, dogs with points in guard dog will have armour.").getBoolean(false);
        ConfigValues.MOD_BED_STUFF = CONFIG.get(CATEGORY_DOGGYSETTINGS, "modBedStuff", true, "When enabled, some mods that add new planks will be able to be used for dog beds.").getBoolean(true);
        ConfigValues.PUPS_GET_PARENT_LEVELS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "pupsGetParentLevel", false, "When enabled, puppies get some levels from parents. When disabled, puppies start at 0 points").getBoolean(false);
        ConfigValues.DOG_RESPAWN = CONFIG.get(CATEGORY_DOGGYSETTINGS,"dogRespawn",true,"When enabled, dogs can be linked to a dog bed to respawn").setRequiresMcRestart(true).getBoolean(true);
        //Add Everything in the current list in whatever way you want
        orderDSetting.add("isDogImmortal");
        orderDSetting.add("tenDayPuppies");
        orderDSetting.add("isHungerOn");
        orderDSetting.add("dogGender");
        orderDSetting.add("shouldWhineWhenStarving");
        orderDSetting.add("pupsGetParentLevel");
        orderDSetting.add("isStartingItemsEnabled");
        orderDSetting.add("modBedStuff");
        
        orderDSetting.add("direParticles");
        orderDSetting.add("bloodWhenIncapacitated");
        orderDSetting.add("doggyChest");
        orderDSetting.add("doggySaddle");
        orderDSetting.add("doggyWings");
        orderDSetting.add("doggyArmour");
        
        
        //Sets the category property order to that of which you have set the list above
        CONFIG.setCategoryPropertyOrder(CATEGORY_DOGGYSETTINGS, orderDSetting);
        
        ConfigValues.DISABLED_TALENTS.clear();
        
        String[] talentIds = new String[] {TalentNames.BED_FINDER, TalentNames.BLACK_PELT, TalentNames.CREEPER_SWEEPER, TalentNames.DOGGY_DASH, TalentNames.FISHER_DOG, 
                TalentNames.GUARD_DOG, TalentNames.HAPPY_EATER, TalentNames.HELL_HOUND, TalentNames.HUNTER_DOG, TalentNames.PACK_PUPPY, TalentNames.PEST_FIGHTER, 
                TalentNames.PILLOW_PAW, TalentNames.POISON_FANG, TalentNames.PUPPY_EYES, TalentNames.QUICK_HEALER, TalentNames.RESCUE_DOG, TalentNames.ROARING_GALE,
                TalentNames.SHEPHERD_DOG, TalentNames.SWIMMER_DOG, TalentNames.WOLF_MOUNT};
        for(String talentId : talentIds) {
            boolean enabled = CONFIG.get(CATEGORY_TALENT, talentId, true).getBoolean(true);
            if(!enabled)
                ConfigValues.DISABLED_TALENTS.add(talentId);
        }

        List<String> roaringSettings = new ArrayList<>();

        ConfigValues.TALENT_ROAR_EFFECT_DURATION_BASE = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarEffectDurationBase", 20, "The time in ticks for roar effect duration at level 1, default 20 (1 second) and minimum 0", 0, Short.MAX_VALUE).getInt();
        ConfigValues.TALENT_ROAR_EFFECT_DURATION_LEVEL_UP = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarEffectDurationLevelUp", 12, "The time in ticks for roar effect duration increase per level, default 12 (0.6 second) and minimum 0", 0, Short.MAX_VALUE).getInt();
        ConfigValues.TALENT_ROAR_EFFECT_DURATION_LEVEL_UP_5 = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarEffectDurationLevel5", 70, "The time in ticks for roar effect duration at level 5, default 70 (3.5 second) and minimum 0", 0, Short.MAX_VALUE).getInt();
        ConfigValues.TALENT_ROAR_DAMAGE = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarDamage", 1, "Damage dealt to mobs when roaring, default 1 and minimum 0", 0, Float.MAX_VALUE).getDouble();
        ConfigValues.TALENT_ROAR_DAMAGE_LEVEL_5_MULTIPLIER = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarDamageLevel5Multiplier", 2, "Damage multiplier at level 5, default 2 and minimum 1", 1, Float.MAX_VALUE).getDouble();
        ConfigValues.TALENT_ROAR_COOLDOWN = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarCooldown", 100, "The time in ticks for roar cooldown , default 100 and minimum 0", 0, Short.MAX_VALUE).getInt();
        ConfigValues.TALENT_ROAR_COOLDOWN_LEVEL_5 = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarCooldownLevel5", 60, "The time in ticks for roar cooldown when level 5 , default 60 and minimum 0", 0, Short.MAX_VALUE).getInt();
        ConfigValues.TALENT_ROAR_RANGE = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarRange", 4, "Roar range increase per level, default 4, min 1, and max 10", 1, 10).getInt();
        ConfigValues.TALENT_ROAR_HUNGER_COST = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarHungerCost", 80, "Roar hunger cost, default 80, set 0 to disable hunger cost", 0, Short.MAX_VALUE).getInt();
        ConfigValues.TALENT_ROAR_HUNGER_COST_5 = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarHungerCost5", 60, "Roar hunger cost at level 5, default 60, set 0 to disable hunger cost", 0, Short.MAX_VALUE).getInt();
        ConfigValues.TALENT_ROAR_UNLIMITED_HIGH = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarUnlimitedHigh", true, "When enabled, the roar will have unlimited height").getBoolean(true);
        ConfigValues.TALENT_ROAR_BYPASSES_ARMOR = CONFIG.get(CATEGORY_ROARING_GALE_SETTING, "roarBypassesArmor", true, "When enabled, roar damage will bypass armor").getBoolean(true);

        roaringSettings.add("roarEffectDurationBase");
        roaringSettings.add("roarEffectDurationLevelUp");
        roaringSettings.add("roarEffectDurationLevel5");
        roaringSettings.add("roarDamage");
        roaringSettings.add("roarDamageLevel5Multiplier");
        roaringSettings.add("roarCooldown");
        roaringSettings.add("roarCooldownLevel5");
        roaringSettings.add("roarRange");
        roaringSettings.add("roarHungerCost");
        roaringSettings.add("roarHungerCost5");
        roaringSettings.add("roarBypassesArmor");
        CONFIG.setCategoryPropertyOrder(CATEGORY_ROARING_GALE_SETTING, roaringSettings);

        List<String> rescueSettings = new ArrayList<>();
        ConfigValues.TALENT_RESCUE_HEAL_BASE = CONFIG.get(CATEGORY_RESCUE_DOG_SETTING, "rescueHealBase", 1.5, "Rescue dog heal amount increase per level, default 1.5 and minimum 0", 0, Float.MAX_VALUE).getDouble();
        ConfigValues.TALENT_RESCUE_HUNGER_COST = CONFIG.get(CATEGORY_RESCUE_DOG_SETTING, "rescueHungerCost", 100, "Rescue hunger cost, default 100, set 0 to disable hunger cost", 0, Short.MAX_VALUE).getInt();
        ConfigValues.TALENT_RESCUE_HUNGER_COST_5 = CONFIG.get(CATEGORY_RESCUE_DOG_SETTING, "rescueHungerCost5", 80, "Rescue hunger cost at level 5, default 80, set 0 to disable hunger cost", 0, Short.MAX_VALUE).getInt();

        rescueSettings.add("rescueHealBase");
        rescueSettings.add("rescueHungerCost");
        rescueSettings.add("rescueHungerCost5");
        CONFIG.setCategoryPropertyOrder(CATEGORY_RESCUE_DOG_SETTING, rescueSettings);
        
        if(CONFIG.hasChanged())
            CONFIG.save();
    }
}