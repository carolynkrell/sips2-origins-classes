package io.github.apace100.originsclasses.mixin;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.power.VariableIntPower;
import io.github.apace100.originsclasses.effect.StealthEffect;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.FluidTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow @Final public PlayerScreenHandler playerScreenHandler;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", ordinal = 3))
    private void removeSprintingExhaustion(PlayerEntity playerEntity, float exhaustion) {
        if (!ClassPowerTypes.DUDUDUDU.isActive(playerEntity)) {
        	playerEntity.addExhaustion(exhaustion);
        }
    }

    @ModifyVariable(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F"), ordinal = 0)
    private float modifyBaseAttackDamageInStealth(float originalAttackDamage, Entity target) {
        float modifiedDamage = originalAttackDamage;
        if (target instanceof Monster) {
    		if (ClassPowerTypes.COMBAT_SPECIALIST.isActive(this)) {
    			modifiedDamage *= 2;
    			Origins.LOGGER.info("Dealing double damage: " + originalAttackDamage + " -> " + modifiedDamage);
    		}
    		else if (ClassPowerTypes.MORE_AXE_DAMAGE.isActive(this)) {
    			if (this.getMainHandStack().getItem() instanceof AxeItem) {
    				modifiedDamage += 1;
        			Origins.LOGGER.info("Dealing extra damage: " + originalAttackDamage + " -> " + modifiedDamage);
    			}
    		}
    	}
        return modifiedDamage;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickStealthCounter(CallbackInfo ci) {
        if(ClassPowerTypes.STEALTH.isActive(this)) {
            VariableIntPower stealthCounter = ClassPowerTypes.STEALTH.get(this);
            if(this.isSneaking()) {
                if(stealthCounter.increment() == stealthCounter.getMax()) {
                    if(!this.hasStatusEffect(StealthEffect.INSTANCE)) {
                        this.addStatusEffect(new StatusEffectInstance(StealthEffect.INSTANCE, 33000, 0, false, false, true));
                    }
                }
            } else {
                stealthCounter.setValue(stealthCounter.getMin());
                if(this.hasStatusEffect(StealthEffect.INSTANCE)) {
                    this.removeStatusEffect(StealthEffect.INSTANCE);
                }
            }
        }
    }
    
    @Inject(method = "playSound(Lnet/minecraft/sound/SoundEvent;FF)V", at = @At("HEAD"), cancellable = true)
    private void muffleSoundsInStealth(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        if(this.hasStatusEffect(StealthEffect.INSTANCE)) {
            ci.cancel();
        }
    }

    @Redirect(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void muffleEatingFinishSound(World world, PlayerEntity player, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        if(!this.hasStatusEffect(StealthEffect.INSTANCE)) {
            world.playSound(player, x, y, z, sound, category, volume, pitch);
        }
    }
    
    @Inject(method = "isUsingEffectiveTool", at = @At(value = "RETURN"), cancellable = true)
    private void modifyEffectiveTool(CallbackInfoReturnable<Boolean> ci) {
    	PlayerEntity pe = (PlayerEntity) (Object) this;
    	if (pe.inventory.getMainHandStack().getItem() instanceof PickaxeItem) {
    		PickaxeItem pi = (PickaxeItem) pe.inventory.getMainHandStack().getItem();
    		if (pi.getMaterial() == ToolMaterials.GOLD && ClassPowerTypes.MIDAS_TOUCH.isActive(pe)) {
    			ci.setReturnValue(true);
    		}
    	}
    }
    
    @Inject(method = "getBlockBreakingSpeed", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void getBlockBreakingSpeed$OriginsClasses(BlockState bs, CallbackInfoReturnable<Float> ci, float f) {
    	PlayerEntity pe = (PlayerEntity) (Object) this;
    	ItemStack is = pe.inventory.getMainHandStack();
    	if (ClassPowerTypes.FASTER_HARVEST.isActive(pe) && is.getItem() == Items.DIAMOND_AXE && EnchantmentHelper.getEfficiency(pe) == 5 && bs.getMaterial() == Material.WOOD) {
    		f *= 3;
    		ci.setReturnValue(f);
    	}
        if (ClassPowerTypes.SNAIL_SPECIALIST.isActive(pe)) {
            if (this.isSubmergedIn(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
                f *= 5.0F;
            }
            ci.setReturnValue(f);
        }
    }
}
