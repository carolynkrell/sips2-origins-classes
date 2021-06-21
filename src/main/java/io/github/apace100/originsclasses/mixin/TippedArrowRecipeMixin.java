package io.github.apace100.originsclasses.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.TippedArrowRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {


    @Inject(method = "matches", at = @At(value = "RETURN", ordinal = 2), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void matches$OriginsClasses(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> cir, int i, int j, ItemStack itemStack) {
        if (itemStack.isOf(Items.SPLASH_POTION)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "craft", at = @At(value = "RETURN", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void craft$OriginsClasses(CraftingInventory craftingInventory, CallbackInfoReturnable<ItemStack> cir, ItemStack itemstack) {
        if (itemstack.isOf(Items.SPLASH_POTION)) {
            ItemStack itemStack2 = new ItemStack(Items.TIPPED_ARROW, 8);
            PotionUtil.setPotion(itemStack2, PotionUtil.getPotion(itemstack));
            PotionUtil.setCustomPotionEffects(itemStack2, PotionUtil.getCustomPotionEffects(itemstack));
            cir.setReturnValue(itemStack2);
        }
    }
}
