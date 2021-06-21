package io.github.apace100.originsclasses.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.apace100.originsclasses.power.ClassPowerTypes;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntity implements Angerable, Flutterer {

	protected BeeEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
		// TODO Auto-generated constructor stub
	}

	@Inject(method = "tryAttack", at = @At(value = "HEAD"), cancellable = true)
		public void tryAttack$OriginsClasses(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        	if (ClassPowerTypes.BEE_LIKA_DA_YOU.isActive(entity)) {
        		this.stopAnger();
        		ci.setReturnValue(false);
        	}
		}
}
