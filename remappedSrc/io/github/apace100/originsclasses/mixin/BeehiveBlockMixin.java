package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin extends BlockWithEntity {

	protected BeehiveBlockMixin(Settings settings) {
		super(settings);
		// TODO Auto-generated constructor stub
	}
    
    @Accessor("HONEY_LEVEL")
    public static IntProperty getHoneyLevel() {
    	throw new AssertionError();
    }

	@ModifyVariable(method = "angerNearbyBees", at = @At("STORE"), index = 4)
    public List<PlayerEntity> angerNearbyBees$OriginsClasses(List<PlayerEntity> list) {
        list.removeIf(ClassPowerTypes.BEE_LIKA_DA_YOU::isActive);
        return list;
    }
    
    @SuppressWarnings("static-access")
	@Inject(method="onBreak", at = @At(value = "HEAD"), cancellable = true)
    	public void onBreak$OriginsClasses(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
    		if (!world.isClient && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && ClassPowerTypes.RIP_THE_HIVE.isActive(player)) {
    			BlockEntity blockEntity = world.getBlockEntity(pos);
    	         if (blockEntity instanceof BeehiveBlockEntity) {
    	            BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
    	            ItemStack itemStack = new ItemStack(this);
    	            int i = (Integer)state.get(this.getHoneyLevel());
    	            boolean bl = !beehiveBlockEntity.hasNoBees();

    	            NbtCompound compoundTag2;
    	            if (bl) {
    	               compoundTag2 = new NbtCompound();
    	               compoundTag2.put("Bees", beehiveBlockEntity.getBees());
    	               itemStack.putSubTag("BlockEntityTag", compoundTag2);
    	            }

    	            compoundTag2 = new NbtCompound();
    	            compoundTag2.putInt("honey_level", i);
    	            itemStack.putSubTag("BlockStateTag", compoundTag2);
    	            ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStack);
    	            itemEntity.setToDefaultPickupDelay();
    	            world.spawnEntity(itemEntity);
    	            
    	            super.onBreak(world, pos, state, player);
    	            
    	            ci.cancel();
    	         }
    		}
    }
}
