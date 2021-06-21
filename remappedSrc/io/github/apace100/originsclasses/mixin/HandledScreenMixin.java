package io.github.apace100.originsclasses.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {

	protected HandledScreenMixin(Text title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	protected int backgroundWidth = 176;
	protected int backgroundHeight = 166;

	@SuppressWarnings("rawtypes")
	@Inject(method = "isClickOutsideBounds", at = @At(value = "HEAD"), cancellable = true)
		public void isClickOutsideBounds$OriginsClasses(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> ci) {
		if (((HandledScreen) (Object) this) instanceof HorseScreen) {
			HorseScreen hs = (HorseScreen) (HandledScreen) (Object) this;
			if (((HorseScreenAccessor) hs).getEntity() instanceof MuleEntity || ((HorseScreenAccessor) hs).getEntity() instanceof DonkeyEntity) {
				ci.setReturnValue(mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth + 72) || mouseY >= (double)(top + this.backgroundHeight) || (mouseX >= (double)(left + this.backgroundWidth) && mouseY >= (double)(top + 81)));
			}
		}
	}
}
