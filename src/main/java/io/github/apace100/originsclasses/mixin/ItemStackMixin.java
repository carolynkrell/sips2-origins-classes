package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	
	@Shadow public abstract boolean hasTag();
	
	@Shadow public abstract NbtCompound getTag();
	
	@Inject(method = "getName", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
	private void getExtendedName(CallbackInfoReturnable<Text> cir) {
		if (hasTag() && getTag().contains("OriginalName")) {
			cir.setReturnValue(Text.Serializer.fromJson(getTag().getString("OriginalName")));
		}
	}
	
	@Inject(method = "damage", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private void damage$OriginsClasses(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> ci) {
		if (((ItemStack) (Object) this).getItem() instanceof ToolItem) {
			ToolItem ti = (ToolItem) ((ItemStack) (Object) this).getItem();
			if (ti.getMaterial() == ToolMaterials.GOLD && ClassPowerTypes.MIDAS_TOUCH.isActive(player)) {
				int r = random.nextInt(51);
				if (!(r == 50)) {
					ci.setReturnValue(false);
				}
			}
			else if (((ItemStack) (Object) this).getItem() == Items.SHEARS && ClassPowerTypes.SNIP_SNIP.isActive(player)) {
				ci.setReturnValue(false);
			}
		}
	}
}
