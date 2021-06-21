package io.github.apace100.originsclasses.mixin;

import com.mojang.authlib.GameProfile;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow public abstract Hand getActiveHand();

    @ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 0.2F))
    private float modifyItemUseSlowdown(float originalSlowdown) {
        ItemStack stackInUse = this.getStackInHand(this.getActiveHand());
        if(stackInUse.getItem() instanceof ShieldItem && ClassPowerTypes.LESS_SHIELD_SLOWDOWN.isActive(this)) {
            return 0.6F;
        }
        if(stackInUse.getItem() instanceof BowItem && ClassPowerTypes.LESS_BOW_SLOWDOWN.isActive(this)) {
            return 0.6F;
        }
        return originalSlowdown;
    }
}
