package io.github.apace100.originsclasses.mixin;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import io.github.apace100.originsclasses.power.CraftAmountPower;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.*;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {

    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void modifyCraftingResult(int syncId, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo ci, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
        if(itemStack.getItem().isFood() && ClassPowerTypes.THERE_GOES_THE_BAKER.isActive(player)) {
            FoodComponent food = itemStack.getItem().getFoodComponent();
            int foodBonus = (int)Math.ceil((float)food.getHunger() / 3F);
            if(foodBonus < 1) {
                foodBonus = 1;
            }
            itemStack.getOrCreateTag().putInt("FoodBonus", foodBonus);
        }
        if(ClassPowerTypes.FORGE_PORT.isActive(player) && isEquipment(itemStack)) {
            addQualityAttribute(itemStack);
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
        int newValue = (int) OriginComponent.modify(player, CraftAmountPower.class, baseValue, (p -> p.doesApply(itemStack)));
        if(newValue != baseValue) {
            itemStack.setCount(newValue < 0 ? 0 : Math.min(newValue, itemStack.getMaxCount()));
        }
    }

    private static void addQualityAttribute(ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof ArmorItem) {
            EquipmentSlot slot = ((ArmorItem)item).getSlotType();
            stack.addAttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier("Blacksmith quality", 0.25D, EntityAttributeModifier.Operation.ADDITION), slot);
        } else if(item instanceof SwordItem || item instanceof RangedWeaponItem) {
            stack.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier("Blacksmith quality", 0.5D, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
        } else if(item instanceof MiningToolItem || item instanceof ShearsItem) {
            stack.getOrCreateTag().putFloat("MiningSpeedMultiplier", 1.05F);
        } else if(item instanceof ShieldItem) {
            stack.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier("Blacksmith quality", 0.1D, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.OFFHAND);
        }
    }

    private static boolean isEquipment(ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof ArmorItem)
            return true;
        if(item instanceof ToolItem)
            return true;
        if(item instanceof RangedWeaponItem)
            return true;
        if(item instanceof ShieldItem)
            return true;
        return false;
    }
}
