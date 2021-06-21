package io.github.apace100.originsclasses.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {
	
	@Inject(method = "angerBees", at = @At("HEAD"), cancellable = true)
	    public void angerBees$OriginsClasses(@Nullable PlayerEntity player, BlockState state, BeehiveBlockEntity.BeeState beeState, CallbackInfo ci) {
	        if (ClassPowerTypes.BEE_LIKA_DA_YOU.isActive(player)) {
	            ci.cancel();
	            return;
	        }
	   }
}
