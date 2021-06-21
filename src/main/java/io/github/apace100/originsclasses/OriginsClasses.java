package io.github.apace100.originsclasses;

import io.github.apace100.originsclasses.condition.ClassesBlockConditions;
import io.github.apace100.originsclasses.effect.StealthEffect;
import io.github.apace100.originsclasses.power.ClassesPowerFactories;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class OriginsClasses implements ModInitializer {

	public static final String MODID = "origins-classes";

	// Mixin Save States :) Very useful, not hacky :)
	public static boolean isClericEnchanting;

	@Override
	public void onInitialize() {
		ClassesPowerFactories.register();
		//ClassPowerTypes.register();
		ClassesBlockConditions.register();
		Registry.register(Registry.STATUS_EFFECT, new Identifier(MODID, "stealth"), StealthEffect.INSTANCE);
	}

	public static EquipmentSlot[] initEquipmentSlotArray() {
		return new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
	}

	public static StatusEffect[] initDoctorPotionsArray() {
		return new StatusEffect[] { StatusEffects.INSTANT_HEALTH, StatusEffects.INSTANT_DAMAGE, StatusEffects.REGENERATION, StatusEffects.POISON };
	}

	public static Potion[] initAlcoholPotionsArray() {
		return new Potion[] { Potions.MUNDANE, Potions.AWKWARD, Potions.THICK };
	}

	public static String[] initAlcoholPotionsStringArray() {
		return new String[] { "beer", "shirley_temple", "whiskey" };
	}
}
