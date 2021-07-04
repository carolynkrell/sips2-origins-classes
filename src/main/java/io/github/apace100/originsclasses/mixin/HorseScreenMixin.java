package io.github.apace100.originsclasses.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {

	
	private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/horse.png");
	private static final Identifier TEXTURE2 = new Identifier("origins-classes", "textures/gui/container/horse2.png");

	public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		// TODO Auto-generated constructor stub
	}
	
	@Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HorseScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
		public void drawBackground$OriginsClasses(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci, int i, int j) {
			HorseBaseEntity hbe = ((HorseScreenAccessor) (Object) this).getEntity();
			if (hbe instanceof MuleEntity || hbe instanceof DonkeyEntity) {
				RenderSystem.setShaderTexture(0, TEXTURE2);
				((HorseScreen) (Object) this).drawTexture(matrices, i+this.backgroundWidth, j, this.backgroundWidth, 0, 72, 81);
			}
			else {
				RenderSystem.setShaderTexture(0,TEXTURE);
			}
	}
}
