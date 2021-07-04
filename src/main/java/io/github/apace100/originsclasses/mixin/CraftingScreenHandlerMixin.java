package io.github.apace100.originsclasses.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import io.github.apace100.originsclasses.power.CraftAmountPower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {

    private static Random random = new Random();

    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void modifyCraftingResult(ScreenHandler screenHandler, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo ci, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
        if(itemStack.getItem().isFood() && ClassPowerTypes.THERE_GOES_THE_BAKER.isActive(player)) {
            FoodComponent food = itemStack.getItem().getFoodComponent();
            int foodBonus = (int)Math.ceil((float)food.getHunger() / 3F);
            if(foodBonus < 1) {
                foodBonus = 1;
            }
            itemStack.getOrCreateTag().putInt("FoodBonus", foodBonus);
        }
        if (itemStack.getItem() == Items.BONE_MEAL && ClassPowerTypes.FLOWER_POWER.isActive(player)) {
            itemStack.getOrCreateTag().putBoolean("BetterBonemeal", true);
        }
        if (itemStack.getItem() == Items.CROSSBOW && !ClassPowerTypes.MUNITIONS_EXPERT.isActive(player)) {
            itemStack.setCount(0);
        }
        if (itemStack.getItem() == Items.TIPPED_ARROW && !ClassPowerTypes.MUNITIONS_EXPERT.isActive(player)) {
            itemStack.setCount(0);
        }
        int baseValue = itemStack.getCount();
        int newValue = (int) PowerHolderComponent.modify(player, CraftAmountPower.class, baseValue, (p -> p.doesApply(itemStack)));
        if(newValue != baseValue) {
            itemStack.setCount(newValue < 0 ? 0 : Math.min(newValue, itemStack.getMaxCount()));
        }
    }
}
