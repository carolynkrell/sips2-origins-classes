package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin {

    private static Random random = new Random();

    @Inject(method = "onUse", at = @At(value = "HEAD"), cancellable = true)
    private void onUse$OriginsClasses(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient) {
            cir.setReturnValue(ActionResult.SUCCESS);
        }
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.hasTag() && itemStack.getTag().getBoolean("BlacksmithHeated") && ClassPowerTypes.FORGE_PORT.isActive(player) && isEquipment(itemStack)) {
            ItemStack is2 = itemStack.copy();
            itemStack.decrement(1);
            is2.setCount(1);
            is2.getOrCreateTag().putBoolean("BlacksmithHeated", false);
            addQualityAttribute(is2);
            player.giveItemStack(is2);
            cir.setReturnValue(ActionResult.SUCCESS);
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

    private static void addQualityAttribute(ItemStack stack) {
        Item item = stack.getItem();
        stack.getOrCreateTag().putBoolean("BlacksmithBonus", true);
        if(item instanceof ArmorItem) {
            EquipmentSlot slot = ((ArmorItem)item).getSlotType();
            stack.addAttributeModifier(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier("Blacksmith quality", ((ArmorItem) item).getProtection(), EntityAttributeModifier.Operation.ADDITION), slot);
            stack.addAttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier("Blacksmith quality", random.nextDouble() * 1.5 + ((ArmorItem) item).getToughness(), EntityAttributeModifier.Operation.ADDITION), slot);
        } else if(item instanceof SwordItem) {
            stack.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier("Blacksmith Sword Speed", -2.4000000953674316, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
            stack.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier("Blacksmith Sword Damage", ((double)((SwordItem) item).getAttackDamage()) + random.nextDouble() * 0.5 + 1, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
        } else if(item instanceof RangedWeaponItem) {
            stack.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier("Blacksmith quality", random.nextDouble() * 1.5, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
        } else if(item instanceof MiningToolItem || item instanceof ShearsItem) {
            stack.getOrCreateTag().putFloat("MiningSpeedMultiplier", 1.05F);
        } else if(item instanceof ShieldItem) {
            stack.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier("Blacksmith quality", 0.1D, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.OFFHAND);
        }
    }
}
