package io.github.apace100.originsclasses.mixin;

import io.github.apace100.origins.Origins;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FishingRodItem.class)
public abstract class FishingRodItemMixin extends Item implements Vanishable {

	public FishingRodItemMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "use", at = @At(value = "INVOKE_ASSIGN", target="Lnet/minecraft/enchantment/EnchantmentHelper;getLuckOfTheSea(Lnet/minecraft/item/ItemStack;)I", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
		public void tryAttack$OriginsClasses(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, ItemStack itemStack, int i, int k) {
        	if (ClassPowerTypes.ANGLER.isActive(user)) {
				if (!(i > 0)) {
					i = 1;
					Origins.LOGGER.info("Set Lure to 1");
				}
				if (!(k > 0)) {
					k = 1;
					Origins.LOGGER.info("Set LotS to 1");
				}
			}
		}
}
