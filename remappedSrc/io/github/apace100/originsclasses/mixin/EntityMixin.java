package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "isSneaky", at = @At("HEAD"), cancellable = true)
    private void modifySneakyState(CallbackInfoReturnable<Boolean> cir) {
        if(ClassPowerTypes.SNEAKY.isActive((Entity)(Object)this)) {
            cir.setReturnValue(true);
        }
    }
    
    @Environment(EnvType.CLIENT)
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void makeEntitiesGlow(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Entity thisEntity = (Entity)(Object)this;
        if(player != null && player != thisEntity && thisEntity instanceof Monster) {
        	if (ClassPowerTypes.OH_DREAMMMMMM.isActive(player)) {
        		if (thisEntity.getPos().isInRange(player.getPos(), 12)) {
                	cir.setReturnValue(true);
                }
        	}
        }
    }
}
