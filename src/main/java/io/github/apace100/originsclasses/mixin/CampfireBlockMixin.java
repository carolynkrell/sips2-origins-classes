package io.github.apace100.originsclasses.mixin;

import io.github.apace100.origins.Origins;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import io.github.apace100.originsclasses.registry.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin {

    @Inject(method = "onUse", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    private void onUse$OriginsClasses(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if((!itemStack.hasTag() || !itemStack.getTag().getBoolean("FoodBonus")) && itemStack.getItem().isFood() && ModTags.COOKED_FISH.contains(itemStack.getItem()) && ClassPowerTypes.ANGLER.isActive(player)) {
            Origins.LOGGER.info("Made it here 2");
            ItemStack is2 = itemStack.copy();
            is2.setCount(1);
            FoodComponent food = is2.getItem().getFoodComponent();
            int foodBonus = (int)Math.ceil((float)food.getHunger() / 3F);
            if(foodBonus < 1) {
                foodBonus = 1;
            }
            is2.getOrCreateTag().putInt("FoodBonus", foodBonus);
            itemStack.decrement(1);
            player.giveItemStack(is2);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

}
