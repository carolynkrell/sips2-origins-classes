package io.github.apace100.originsclasses.mixin;

import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.passive.HorseBaseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HorseScreen.class)
public interface HorseScreenAccessor {
	@Accessor
    HorseBaseEntity getEntity();
}
