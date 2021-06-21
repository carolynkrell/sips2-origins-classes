package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.TallSeagrassBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TallPlantBlock.class)
public class TallPlantBlockMixin {

    @Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/TallPlantBlock;dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V"))
    public void onBreak$OriginsClasses(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack) {
        if (((TallPlantBlock)(Object)this) instanceof TallSeagrassBlock) {
            if (ClassPowerTypes.CONSERVATIONALIST.isActive(entity)) {
                ((TallPlantBlock)(Object)this).dropStacks(state, world, pos, (BlockEntity)null, entity, new ItemStack(Items.SHEARS));
            }
        }
    }

}
