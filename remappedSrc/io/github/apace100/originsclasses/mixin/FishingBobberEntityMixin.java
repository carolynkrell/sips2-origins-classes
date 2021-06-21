package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {



	@Inject(method = "use", at = @At(value = "INVOKE", target="Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", shift = At.Shift.AFTER))
		public void tryAttack$OriginsClasses(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
			PlayerEntity playerEntity = ((FishingBobberEntity)(Object)this).getPlayerOwner();
        	if (ClassPowerTypes.EXPERIENCED.isActive(playerEntity)) {
				playerEntity.world.spawnEntity(new ExperienceOrbEntity(playerEntity.world, playerEntity.getX(), playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, (new Random().nextInt(6) + 1)));
			}
		}
}
