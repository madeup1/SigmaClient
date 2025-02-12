package git.sigmaclient.mixins.render;

import git.sigmaclient.SigmaClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.EntityVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ RenderVillager.class })
public class MixinRenderVillager
{
    @Inject(method = { "preRenderCallback(Lnet/minecraft/entity/passive/EntityVillager;F)V" }, at = { @At("HEAD") }, cancellable = true)
    private <T extends EntityVillager> void onPreRenderCallback(final T entitylivingbaseIn, final float partialTickTime, final CallbackInfo ci) {
        if (SigmaClient.giants != null && SigmaClient.giants.isToggled() && SigmaClient.giants.mobs.isEnabled()) {
            GlStateManager.scale(SigmaClient.giants.scale.getValue(), SigmaClient.giants.scale.getValue(), SigmaClient.giants.scale.getValue());
        }
    }
}
