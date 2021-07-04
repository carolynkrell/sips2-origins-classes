package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.OriginsClasses;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
		// TODO Auto-generated constructor stub
	}

	@Inject(method = "dropLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void dropAdditionalRancherLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci, Identifier identifier, LootTable lootTable, LootContext.Builder builder) {
        if(causedByPlayer && (Object)this instanceof AnimalEntity && ClassPowerTypes.HARD_WORKIN.isActive(source.getAttacker())) {
            if(new Random().nextInt(10) < 5) {
                lootTable.generateLoot(builder.build(LootContextTypes.ENTITY), ((LivingEntity)(Object)this)::dropStack);
            }
        }
        else if(causedByPlayer && (Object)this instanceof SquidEntity && ClassPowerTypes.CATCH_AND_RELEASE.isActive(source.getAttacker())) {
            if(new Random().nextInt(10) < 5) {
                lootTable.generateLoot(builder.build(LootContextTypes.ENTITY), ((LivingEntity)(Object)this)::dropStack);
            }
        }
        else if(causedByPlayer && (Object)this instanceof Monster && ClassPowerTypes.ONE_IN_75_TRILLION.isActive(source.getAttacker())) {
            if(new Random().nextInt(10) < 5) {
                lootTable.generateLoot(builder.build(LootContextTypes.ENTITY), ((LivingEntity)(Object)this)::dropStack);
            }
        }
    }
    
    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("RETURN"))
    private void addStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> ci) {
        if (ci.getReturnValue() && !effect.isAmbient()) {
            if(ClassPowerTypes.TAMED_POTION_DIFFUSAL.isActive(this)) {
                world.getEntitiesByClass(TameableEntity.class, getBoundingBox().stretch(8F, 2F, 8F).stretch(-8f, -2F, -8F), e -> e.getOwner() == (Object) this).forEach(e -> e.addStatusEffect(effect));
            }
        }
    }
    
    
    @Inject(method = "getNextAirUnderwater", at = @At(value = "HEAD"), cancellable = true)
    	private void getNextAirUnderwater$OriginsClasses(int air, CallbackInfoReturnable<Integer> ci) {
		int i = EnchantmentHelper.getRespiration((LivingEntity) (Object) this);
		if (ClassPowerTypes.BUILT_DIFFERENT.isActive(this) || ClassPowerTypes.SNAIL_SPECIALIST.isActive(this)) {
			if (i == 0) {
				i++;
			}
		}
		ci.setReturnValue(i > 0 && this.random.nextInt(i + 1) > 0 ? air : air - 1);
    }
    
    
	@SuppressWarnings("unlikely-arg-type")
	@ModifyVariable(method = "applyEnchantmentsToDamage", at = @At(value = "INVOKE_ASSIGN", target="Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Ljava/lang/Iterable;Lnet/minecraft/entity/damage/DamageSource;)I", shift = At.Shift.AFTER), ordinal = 0)
    	private int applyEnchantmentsToDamage$OriginsClasses(int originalK, DamageSource source, float amount) {
    		int modifiedK = 0;
    		if (ClassPowerTypes.BUILT_DIFFERENT.isActive(this)) {
    			if (source == DamageSource.FALL) {
    				if (((LivingEntity) (Object) this).getEquippedStack(EquipmentSlot.FEET) != null) {
        				ItemStack is = ((LivingEntity) (Object) this).getEquippedStack(EquipmentSlot.FEET);
        				if (!is.getEnchantments().contains(Enchantments.FEATHER_FALLING)) {
        					modifiedK += 3;
        				}
        			}
    				else {
    					modifiedK += 3;
    				}
    			}
    		}
    		return modifiedK;
    	}
    
    
    @Inject(method = "applyArmorToDamage", at = @At("HEAD"), cancellable = true)
    	private void applyArmorToDamage$OriginsClasses(DamageSource source, float amount, CallbackInfoReturnable<Float> ci) {
    		if (!source.bypassesArmor()) {
    			LivingEntity li = (LivingEntity) (Entity) this;
				int baseArmor = li.getArmor();
				double baseToughness = li.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
				
    			if (ClassPowerTypes.APIARIST.isActive(this)) {
    				double editedToughness = calculateToughness(baseToughness, ArmorMaterials.CHAIN, ArmorMaterials.DIAMOND);

    				int r = random.nextInt(100);
					if (r >= 75) {
						this.invokeDamageArmor(source, amount);
					}

    				amount = DamageUtil.getDamageLeft(amount, (float)baseArmor, (float)editedToughness);
    				ci.setReturnValue(amount);
    			}
    			else if (ClassPowerTypes.MIDAS_TOUCH.isActive(this)) {
    				double editedToughness = calculateToughness(baseToughness, ArmorMaterials.GOLD, ArmorMaterials.DIAMOND);

					int r = random.nextInt(100);
					if (r >= 75) {
						this.invokeDamageArmor(source, amount);
					}

					amount = DamageUtil.getDamageLeft(amount, (float)baseArmor, (float)editedToughness);
    				ci.setReturnValue(amount);
    			}
    		}
    	}

	@Inject(method = "getArmor", at = @At("HEAD"), cancellable = true)
	private void getArmor$OriginsClasses(CallbackInfoReturnable<Integer> ci) {
		LivingEntity li = (LivingEntity) (Entity) this;
		int baseArmor = MathHelper.floor(li.getAttributeValue(EntityAttributes.GENERIC_ARMOR));

		if (ClassPowerTypes.APIARIST.isActive(li)) {
			int editedArmor = calculateArmor(baseArmor, ArmorMaterials.CHAIN, ArmorMaterials.DIAMOND);
			ci.setReturnValue(editedArmor);
		}
		else if (ClassPowerTypes.MIDAS_TOUCH.isActive(li)) {
			int editedArmor = calculateArmor(baseArmor, ArmorMaterials.GOLD, ArmorMaterials.DIAMOND);
			ci.setReturnValue(editedArmor);
		}
	}
    
    @Invoker("damageArmor")
    public abstract void invokeDamageArmor(DamageSource source, float amount);
    
    private ArmorItem getAIAtSlot(EquipmentSlot slot) {
    	Item item = ((LivingEntity) (Entity) this).getEquippedStack(slot).getItem();
    	if (item instanceof ArmorItem) {
    		return (ArmorItem) item;
    	}
    	else {
    		return (ArmorItem) null;
    	}
    }
    
    private EquipmentSlot[] slots = OriginsClasses.initEquipmentSlotArray();
    
    private int calculateArmor(int armor, ArmorMaterial original, ArmorMaterial replacement) {
    	for (int i = 0; i < slots.length; i++) {
    		EquipmentSlot es = slots[i];
    		if (!(getAIAtSlot(es) == null)) { 
    			ArmorItem ai = (getAIAtSlot(es));
    			if (ai.getMaterial() == original) {
        			armor -= ai.getProtection();
        			armor += new ArmorItem(replacement, es, new Settings()).getProtection();
        		}
    		}
    	}
    	return armor;
    }
    
    private double calculateToughness(double toughness, ArmorMaterial original, ArmorMaterial replacement) {
    	for (int i = 0; i < slots.length; i++) {
    		EquipmentSlot es = slots[i];
    		if (!(getAIAtSlot(es) == null)) { 
    			ArmorItem ai = (getAIAtSlot(es));
    			if (ai.getMaterial() == original) {
        			toughness -= original.getToughness();
        			toughness += replacement.getToughness();
        		}
    		}
    	}
    	return toughness;
    }
    
}
