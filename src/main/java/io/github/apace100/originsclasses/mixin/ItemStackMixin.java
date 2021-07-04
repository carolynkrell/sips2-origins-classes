package io.github.apace100.originsclasses.mixin;

import com.google.common.collect.Multimap;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.minecraft.item.ItemStack.MODIFIER_FORMAT;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	
	@Shadow public abstract boolean hasTag();
	
	@Shadow public abstract NbtCompound getTag();

	@Shadow public abstract Item getItem();

	@Inject(method = "getName", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
	private void getExtendedName(CallbackInfoReturnable<Text> cir) {
		if (hasTag() && getTag().contains("OriginalName")) {
			cir.setReturnValue(Text.Serializer.fromJson(getTag().getString("OriginalName")));
		}
	}
	
	@Inject(method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void damage$OriginsClasses(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> ci) {
		if (this.getItem() instanceof ToolItem) {
			ToolItem ti = (ToolItem) this.getItem();
			if (ti.getMaterial() == ToolMaterials.GOLD && ClassPowerTypes.MIDAS_TOUCH.isActive(player)) {
				int r = random.nextInt(51);
				if (!(r == 50)) {
					ci.setReturnValue(false);
				}
			}
		}
		else if (this.getItem().equals(Items.SHEARS) && ClassPowerTypes.SNIP_SNIP.isActive(player)) {
			ci.setReturnValue(false);
		}

	}

	@Inject(method = "getTooltip", at = @At(value="INVOKE",target="Ljava/util/List;add(Ljava/lang/Object;)Z", shift= At.Shift.AFTER, ordinal = 8), locals = LocalCapture.CAPTURE_FAILHARD)
	private void getTooltipPlus$OriginsClasses(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list, int l, EquipmentSlot[] var21, int i1, int i2, EquipmentSlot eq, Multimap map, Iterator iterator, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier eam, double d) {
		if (eam.getName().equals("Blacksmith Sword Damage")) {
			list.remove(list.size()-1);
			list.add((new LiteralText(" ")).append(new TranslatableText("attribute.modifier.equals." + eam.getOperation().getId(), new Object[]{MODIFIER_FORMAT.format(d), new TranslatableText(((EntityAttribute)entry.getKey()).getTranslationKey())})).formatted(Formatting.DARK_GREEN));
		}
		else if (eam.getName().equals("Blacksmith Sword Speed")) {
			list.remove(list.size()-1);
			d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
			list.add((new LiteralText(" ")).append(new TranslatableText("attribute.modifier.equals." + eam.getOperation().getId(), new Object[]{MODIFIER_FORMAT.format(d), new TranslatableText(((EntityAttribute)entry.getKey()).getTranslationKey())})).formatted(Formatting.DARK_GREEN));
		}
	}

	@Inject(method = "getTooltip", at = @At(value="INVOKE",target="Ljava/util/List;add(Ljava/lang/Object;)Z", shift= At.Shift.AFTER, ordinal = 9), locals = LocalCapture.CAPTURE_FAILHARD)
	private void getTooltipMinus$OriginsClasses(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list, int l, EquipmentSlot[] var21, int i1, int i2, EquipmentSlot eq, Multimap map, Iterator iterator, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier eam, double d) {
		if (eam.getName().equals("Blacksmith Sword Damage")) {
			list.remove(list.size()-1);
			list.add((new LiteralText(" ")).append(new TranslatableText("attribute.modifier.equals." + eam.getOperation().getId(), new Object[]{MODIFIER_FORMAT.format(d), new TranslatableText(((EntityAttribute)entry.getKey()).getTranslationKey())})).formatted(Formatting.DARK_GREEN));
		}
		else if (eam.getName().equals("Blacksmith Sword Speed")) {
			list.remove(list.size()-1);
			d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
			list.add((new LiteralText(" ")).append(new TranslatableText("attribute.modifier.equals." + eam.getOperation().getId(), new Object[]{MODIFIER_FORMAT.format(d), new TranslatableText(((EntityAttribute)entry.getKey()).getTranslationKey())})).formatted(Formatting.DARK_GREEN));
		}
	}


}
