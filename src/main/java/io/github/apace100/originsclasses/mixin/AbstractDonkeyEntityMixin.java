package io.github.apace100.originsclasses.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractDonkeyEntity.class)
public abstract class AbstractDonkeyEntityMixin extends HorseBaseEntity {

	protected AbstractDonkeyEntityMixin(EntityType<? extends HorseBaseEntity> entityType, World world) {
		super(entityType, world);
		// TODO Auto-generated constructor stub
	}

	
	@Inject(method = "getInventorySize", at = @At(value = "RETURN"), cancellable = true)
		public void getInventorySize$OriginsClasses(CallbackInfoReturnable<Integer> ci) {
			ci.setReturnValue(((AbstractDonkeyEntity) (Object) this).hasChest() ? 29 : super.getInventorySize());
		}
	
	@Inject(method = "getInventoryColumns", at = @At(value = "RETURN"), cancellable = true)
		public void getInventoryColumns$OriginsClasses(CallbackInfoReturnable<Integer> ci) {
			ci.setReturnValue(9);
		}
}
