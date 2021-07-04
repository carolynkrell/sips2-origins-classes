package io.github.apace100.originsclasses.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(Item.class)
public interface ItemAccessor {

    @Accessor
    UUID getATTACK_DAMAGE_MODIFIER_ID();

    @Accessor
    UUID getATTACK_SPEED_MODIFIER_ID();
}