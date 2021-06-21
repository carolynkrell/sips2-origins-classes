package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.OriginsClasses;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(AbstractCauldronBlock.class)
public abstract class CauldronBlockMixin {

    @Shadow @Final public static IntProperty LEVEL;

    @Shadow public abstract void setLevel(World world, BlockPos pos, BlockState state, int level);

    @Inject(method = "onUse", at = @At(value = "RETURN", ordinal = 9), cancellable = true)
    private void extendPotionDuration(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {

            ItemStack stack = player.getStackInHand(hand);
            int level = state.get(LEVEL);
            if(stack.getItem() instanceof PotionItem && level > 0 && (!stack.hasTag() || !stack.getTag().getBoolean("IsExtendedByCleric"))) {

                List<StatusEffectInstance> seiList = PotionUtil.getPotionEffects(stack);
                if (isDoctorPotion(seiList)) {
                    if (ClassPowerTypes.WHO.isActive(player)) {
                        ItemStack extended = makeExtendedPotion(stack);
                        setLevel(world, pos, state, level - 1);
                        stack.decrement(1);
                        player.giveItemStack(extended);
                        cir.setReturnValue(ActionResult.SUCCESS);
                    }
                }
                else if(ClassPowerTypes.LONGER_POTIONS.isActive(player)) {
                    int i = isAlcoholPrecursor(stack);
                    if (i != -1) {
                        ItemStack extended = makeAlcoholPotion(stack, i);
                        setLevel(world, pos, state, level - 1);
                        stack.decrement(1);
                        player.giveItemStack(extended);
                        cir.setReturnValue(ActionResult.SUCCESS);
                    }
                    else {
                        ItemStack extended = makeExtendedPotion(stack);
                        setLevel(world, pos, state, level - 1);
                        stack.decrement(1);
                        player.giveItemStack(extended);
                        cir.setReturnValue(ActionResult.SUCCESS);
                    }
                }
            }
        }

    private StatusEffect[] doctorPotions = OriginsClasses.initDoctorPotionsArray();
    private Potion[] alcoholPotions = OriginsClasses.initAlcoholPotionsArray();
    private String[] alcoholPotionsResults = OriginsClasses.initAlcoholPotionsStringArray();

    private boolean isDoctorPotion(List<StatusEffectInstance> seiList) {
        Iterator iterator = seiList.iterator();
        while(iterator.hasNext()) {
            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)iterator.next();
            for (int i = 0; i < doctorPotions.length; i++) {
                if (doctorPotions[i] == statusEffectInstance.getEffectType()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int isAlcoholPrecursor(ItemStack is) {
        Potion p = PotionUtil.getPotion(is);
        for (int i = 0; i < alcoholPotions.length; i++) {
            if (alcoholPotions[i] == p) {
                return i;
            }
        }
        return -1;
    }

    private ItemStack makeAlcoholPotion(ItemStack is, int i) {
        int color = PotionUtil.getColor(is);
        Collection<StatusEffectInstance> customEffect = new ArrayList<>();

        switch (alcoholPotionsResults[i]) {
            case "beer":
                color = 14650627;
                customEffect.add(new StatusEffectInstance(StatusEffects.HUNGER, 20 * 10, 1, false, false, true));
                break;
            case "shirley_temple":
                color = 14622251;
                customEffect.add(new StatusEffectInstance(StatusEffects.ABSORPTION, 20 * 15, 1, false, false, true));
                break;
            case "whiskey":
                color = 11091975;
                customEffect.add(new StatusEffectInstance(StatusEffects.HUNGER, 20 * 10, 1, false, false, true));
                break;
            default:
                break;
        }

        ItemStack extended = new ItemStack(is.getItem());
        NbtCompound tag = extended.getOrCreateTag().copyFrom(is.getTag());
        tag.putBoolean("IsAlcohol", true);
        PotionUtil.setPotion(extended, Potions.EMPTY);
        PotionUtil.setCustomPotionEffects(extended, customEffect);
        tag.putInt("CustomPotionColor", color);
        tag.putString("OriginalName", Text.Serializer.toJson(new TranslatableText(("item.origins-classes."+alcoholPotionsResults[i]+".name"))));
        return extended;
    }

    private ItemStack makeExtendedPotion(ItemStack stack) {
        ItemStack extended = new ItemStack(stack.getItem());
        NbtCompound tag = extended.getOrCreateTag().copyFrom(stack.getTag());
        tag.putString("OriginalName", Text.Serializer.toJson(stack.getName()));
        tag.putBoolean("IsExtendedByCleric", true);
        PotionUtil.setPotion(extended, Potions.EMPTY);
        Collection<StatusEffectInstance> customPotion = (PotionUtil.getCustomPotionEffects(stack).isEmpty() ? PotionUtil.getPotionEffects(stack) : PotionUtil.getCustomPotionEffects(stack)).stream().map(effect -> new StatusEffectInstance(effect.getEffectType(), effect.getDuration() * (effect.getEffectType().isInstant() ? 1 : 2), effect.getAmplifier() * (effect.getEffectType().isInstant() ? 2 : 1), effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon())).collect(Collectors.toList());
        PotionUtil.setCustomPotionEffects(extended, customPotion);
        tag.putInt("CustomPotionColor", PotionUtil.getColor(customPotion));
        return extended;
    }
}
