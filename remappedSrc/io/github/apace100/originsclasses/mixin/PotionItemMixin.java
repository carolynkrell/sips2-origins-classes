package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PotionItem.class)
public class PotionItemMixin extends Item {

    public PotionItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    @Environment(EnvType.CLIENT)
    private void appendExtendedTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if (stack.hasTag() && stack.getTag().contains("IsExtendedByCleric")) {
            tooltip.add(new TranslatableText("origins-classes.longer_potions").formatted(Formatting.GOLD));
        }
        else if (stack.hasTag() && stack.getTag().contains("IsAlcohol")) {
            tooltip.add(new TranslatableText("origins-classes.alcohol").formatted(Formatting.GOLD));
        }
    }

    @Redirect(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"))
    private boolean finishUsing$OriginsClasses(LivingEntity livingEntity, StatusEffectInstance effect) {
        if (ClassPowerTypes.LIGHTWEIGHT.isActive(livingEntity)) {
            StatusEffect se = effect.getEffectType();
            int len = effect.getDuration();
            int amp = effect.getAmplifier();

            return livingEntity.addStatusEffect(new StatusEffectInstance(se, len * 2, amp));
        }
        return livingEntity.addStatusEffect(effect);
    }
}
