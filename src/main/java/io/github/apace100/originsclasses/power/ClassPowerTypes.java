package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.util.Identifier;

public class ClassPowerTypes {

    // UNUSED
    public static final PowerType<Power> SNEAKY = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "sneaky"));
    public static final PowerType<VariableIntPower> STEALTH = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "stealth"));//new PowerType<>((type, player) -> new VariableIntPower(type, player, 0, 0, 200));
    public static final PowerType<Power> LESS_SHIELD_SLOWDOWN = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "less_shield_slowdown"));
    public static final PowerType<Power> TAMED_ANIMAL_BOOST = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "tamed_animal_boost"));
    public static final PowerType<Power> TAMED_POTION_DIFFUSAL = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "tamed_potion_diffusal"));
    public static final PowerType<Power> MORE_SMOKER_XP = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "more_smoker_xp"));
    public static final PowerType<Power> TRADE_AVAILABILITY = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "trade_availability"));
    public static final PowerType<Power> RARE_WANDERING_LOOT = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "rare_wandering_loot"));


    // Baker
    public static final PowerType<Power> THERE_GOES_THE_BAKER = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "there_goes_the_baker"));

    // Banker
    public static final PowerType<Power> MIDAS_TOUCH = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "midas_touch"));
    public static final PowerType<Power> VAULT_KEEPER = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "vault_keeper"));
    
    // Beekeeper
    public static final PowerType<Power> BEE_LIKA_DA_YOU = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "bee_lika_da_you"));
    public static final PowerType<Power> RIP_THE_HIVE = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "rip_the_hive"));
    public static final PowerType<Power> APIARIST = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "apiarist"));
    
    // Botanist
    public static final PowerType<Power> SNIP_SNIP = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "snip_snip"));
    public static final PowerType<Power> FLOWER_POWER = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "flower_power"));
    
    // Blacksmith
    public static final PowerType<Power> FORGE_PORT = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "forge_port"));
    public static final PowerType<Power> EFFICIENT_REPAIRS = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "efficient_repairs"));

    // Doctor
    public static final PowerType<Power> WHO = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "who"));

    // Explorer
    public static final PowerType<Power> DUDUDUDU = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "dudududu"));
    public static final PowerType<Power> BUILT_DIFFERENT = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "built_different"));

    // Fisherman
    public static final PowerType<Power> ANGLER = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "angler"));
    public static final PowerType<Power> EXPERIENCED = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "experienced"));

    // Fletcher
    public static final PowerType<Power> LESS_BOW_SLOWDOWN = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "less_bow_slowdown"));
    public static final PowerType<Power> NO_PROJECTILE_DIVERGENCE = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "gunslinger"));
    public static final PowerType<Power> MUNITIONS_EXPERT = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "munitions_expert"));

    // Hunter
    public static final PowerType<Power> COMBAT_SPECIALIST = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "combat_specialist"));
    public static final PowerType<Power> OH_DREAMMMMMM = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "oh_dreammmmmm"));
    public static final PowerType<Power> ONE_IN_75_TRILLION = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "1_in_75_trillion"));

    // Librarian
    public static final PowerType<Power> ENCHANTED = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "enchanted"));

    // Lumberjack
    public static final PowerType<Power> FASTER_HARVEST = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "faster_harvest"));
    public static final PowerType<Power> MORE_AXE_DAMAGE = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "more_axe_damage"));

    // Malacologist
    public static final PowerType<Power> CATCH_AND_RELEASE = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "catch_and_release"));
    public static final PowerType<Power> SNAIL_SPECIALIST = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "snail_specialist"));
    public static final PowerType<Power> CONSERVATIONALIST = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "conservationalist"));

    // Moonshiner
    public static final PowerType<Power> LIGHTWEIGHT = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "lightweight"));
    public static final PowerType<Power> ALCOHOL_AFICIONADO = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "alcohol_aficionado"));
    
    // Miner
    public static final PowerType<Power> IM_A_MINOR = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "im_a_minor"));
    public static final PowerType<Power> DOWN_IN_THE_DEEP = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "down_in_the_deep"));

    // Rancher
    public static final PowerType<Power> SEX_IMPROVER = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "sex_improver"));
    public static final PowerType<Power> HARD_WORKIN = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "hard_workin"));
    public static final PowerType<Power> COWBOY = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "cowboy"));
    public static final PowerType<Power> MORE_CROP_DROPS = new PowerTypeReference<>(new Identifier(OriginsClasses.MODID, "more_crop_drops"));

}
