package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.origins.power.OriginsCallbackPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StartingEquipmentPower extends OriginsCallbackPower {

    private final List<ItemStack> itemStacks = new LinkedList<>();
    private final HashMap<Integer, ItemStack> slottedStacks = new HashMap<>();

    public StartingEquipmentPower(PowerType<Power> type, LivingEntity entity) {
        super(type, entity, null, null, null, null, null, null, true);
    }


    public void addStack(ItemStack stack) {
        this.itemStacks.add(stack);
    }

    public void addStack(int slot, ItemStack stack) {
        slottedStacks.put(slot, stack);
    }

    @Override
    public void onChosen(boolean isOrbOfOrigin) {
        giveStacks();
    }

    private void giveStacks() {
        slottedStacks.forEach((slot, stack) -> {
            if(entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)entity;
                PlayerInventory inventory = player.getInventory();
                if(inventory.getStack(slot).isEmpty()) {
                    inventory.setStack(slot, stack);
                } else {
                    player.giveItemStack(stack);
                }
            } else {
                entity.dropStack(stack);
            }
        });
        itemStacks.forEach(is -> {
            ItemStack copy = is.copy();
            if(entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)entity;
                player.giveItemStack(copy);
            } else {
                entity.dropStack(copy);
            }
        });
    }
}
