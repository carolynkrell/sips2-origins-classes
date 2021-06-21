package io.github.apace100.originsclasses.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.originsclasses.ducks.SneakingStateSavingManager;
import io.github.apace100.originsclasses.networking.ModPackets;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import io.github.apace100.originsclasses.power.MultiMinePower;
import io.github.apace100.originsclasses.registry.ModTags;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("deprecation")
@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin implements SneakingStateSavingManager {

    @Shadow public ServerWorld world;
    @Shadow public ServerPlayerEntity player;
    @Shadow public abstract void finishMining(BlockPos pos, PlayerActionC2SPacket.Action action, String reason);

    @Shadow private int blockBreakingProgress;
    @Shadow private int startMiningTime;
    private BlockState justMinedBlockState;
    private boolean performingMultiMine = false;
    private boolean wasSneakingWhenStarted = false;
    
    

    @ModifyVariable(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;postMine(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V"), ordinal = 1)
    private boolean modifyEffectiveTool(boolean original) {
    	if (player.getInventory().getMainHandStack().getItem() instanceof PickaxeItem) {
    		PickaxeItem pi = (PickaxeItem) player.getInventory().getMainHandStack().getItem();
    		if (pi.getMaterial() == ToolMaterials.GOLD && ClassPowerTypes.MIDAS_TOUCH.isActive(player)) {
    			return true;
    		}
    	}
        return original;
    }
    
    @Inject(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;postMine(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void postMine$OriginsClasses(BlockPos pos, CallbackInfoReturnable<Boolean> ci, BlockState blockState, BlockEntity blockEntity, Block block, boolean bl, ItemStack itemStack, ItemStack itemStack2, boolean bl2 ) {
    	if (ClassPowerTypes.VAULT_KEEPER.isActive(player) && block == Blocks.ENDER_CHEST) {
    		itemStack.postMine(world, blockState, pos, player);
    		ItemStack is2 = itemStack.copy();
    		is2.addEnchantment(Enchantments.SILK_TOUCH, 1);
    		if (bl && bl2) {
            	block.afterBreak(this.world, this.player, pos, blockState, blockEntity, is2);
        	}
    		ci.setReturnValue(true);
    	}
    	if (ClassPowerTypes.CONSERVATIONALIST.isActive(player) && ModTags.SEA_BLOCKS.contains(block)) {
    		itemStack.postMine(world, blockState, pos, player);
            ItemStack is2;
    		if (block.equals(Blocks.SEAGRASS) || block.equals(Blocks.TALL_SEAGRASS)) {
                is2 = new ItemStack(Items.SHEARS);
            }
    		else {
                is2 = new ItemStack(Items.DIAMOND_PICKAXE);
                is2.addEnchantment(Enchantments.SILK_TOUCH, 1);
            }
            block.afterBreak(this.world, this.player, pos, blockState, blockEntity, is2);
    		ci.setReturnValue(true);
    	}
    }
    
    @Inject(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onBlockBreakStart(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V", ordinal = 0))
    private void saveSneakingState(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, CallbackInfo ci) {
        wasSneakingWhenStarted = player.isSneaking();
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeBoolean(!wasSneakingWhenStarted);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ModPackets.MULTI_MINING, data);
    }

    @Inject(method = "finishMining", at = @At("HEAD"))
    private void saveBlockStateForMultiMine(BlockPos pos, PlayerActionC2SPacket.Action action, String reason, CallbackInfo ci) {
        justMinedBlockState = world.getBlockState(pos);
    }

    @Inject(method = "finishMining", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 0))
    private void multiMinePower(BlockPos pos, PlayerActionC2SPacket.Action action, String reason, CallbackInfo ci) {
        if(!wasSneakingWhenStarted && !performingMultiMine) {
            performingMultiMine = true;
            PowerHolderComponent.KEY.get(player).getPowers(MultiMinePower.class).forEach(mmp -> {
                if(mmp.isBlockStateAffected(justMinedBlockState)) {
                    ItemStack tool = player.getMainHandStack().copy();
                    for(BlockPos bp : mmp.getAffectedBlocks(justMinedBlockState, pos)) {
                        finishMining(bp, action, reason);
                        if(!player.getMainHandStack().isItemEqualIgnoreDamage(tool)) {
                            break;
                        }
                    }
                }
            });
            performingMultiMine = false;
        }
    }

    public boolean wasSneakingWhenBlockBreakingStarted() {
        return wasSneakingWhenStarted;
    }
}
