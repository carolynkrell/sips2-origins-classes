package io.github.apace100.originsclasses.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.github.apace100.originsclasses.power.ClassPowerTypes;

@Mixin(HorseEntity.class)
public abstract class HorseEntityMixin extends HorseBaseEntity {

	protected HorseEntityMixin(EntityType<? extends HorseBaseEntity> entityType, World world) {
		super(entityType, world);
		// TODO Auto-generated constructor stub
	}

	@Inject(method = "createChild", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
		public void createChild$OriginsClasses(ServerWorld world, PassiveEntity entity, CallbackInfoReturnable<PassiveEntity> ci, HorseBaseEntity horseBaseEntity2) {
			Entity mainBreedingPlayer = this.getLovingPlayer();
			Entity otherBreedingPlayer = ((AnimalEntity) entity).getLovingPlayer();
			if (ClassPowerTypes.COWBOY.isActive(mainBreedingPlayer) || ClassPowerTypes.COWBOY.isActive(otherBreedingPlayer)) {
				double d = this.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH) + entity.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH) + (double)this.getChildHealthBonus();
			    horseBaseEntity2.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(d / 3.0D);
			    double e = this.getAttributeBaseValue(EntityAttributes.HORSE_JUMP_STRENGTH) + entity.getAttributeBaseValue(EntityAttributes.HORSE_JUMP_STRENGTH) + this.getChildJumpStrengthBonus();
			    horseBaseEntity2.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH).setBaseValue(e / 3.0D);
			    double f = this.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) + entity.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) + this.getChildMovementSpeedBonus();
			    horseBaseEntity2.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(f / 3.0D);
			    ci.setReturnValue(horseBaseEntity2);
        	}
		}
	
	protected float getChildHealthBonus() {
		return 17.0F + (float)this.random.nextInt(10) + (float)this.random.nextInt(11);
	}

	protected double getChildJumpStrengthBonus() {
		return 0.4200000059604645D + this.random.nextDouble() * 0.4D + this.random.nextDouble() * 0.4D + this.random.nextDouble() * 0.4D;
	}

	protected double getChildMovementSpeedBonus() {
		return (0.46999998807907104D + this.random.nextDouble() * 0.5D + this.random.nextDouble() * 0.5D + this.random.nextDouble() * 0.5D) * 0.25D;
	}
}
