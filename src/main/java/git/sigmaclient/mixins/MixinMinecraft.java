package git.sigmaclient.mixins;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.events.ChestOpenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.entity.Entity;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow private int rightClickDelayTimer;
    @Shadow private Entity renderViewEntity;

    @Inject(method = "startGame", at = @At("TAIL"), cancellable = false)
    public void startGame(CallbackInfo ci)
    {
        SigmaClient.mc = Minecraft.getMinecraft();
    }

    @Inject(method = { "runTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V") })
    public void keyPresses(final CallbackInfo ci) {
        final int k = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
        final char aChar = Keyboard.getEventCharacter();
        if (Keyboard.getEventKeyState()) {
            if (SigmaClient.mc.currentScreen == null) {
                SigmaClient.handleKey(k);
            }
        }
    }

    @Inject(method = { "rightClickMouse" }, at = { @At("RETURN") }, cancellable = true)
    public void onRightClickPost(final CallbackInfo callbackInfo) {
        if (SigmaClient.fastPlace != null && SigmaClient.fastPlace.isToggled()) {
            this.rightClickDelayTimer = (int) SigmaClient.fastPlace.placeDelay.getValue();
        }
    }

    @Mutable
    @Shadow @Final
    private Session session;

    @Inject(at = @At("TAIL"), method = "displayGuiScreen", cancellable = false)
    public void chestEventHook(GuiScreen screen, CallbackInfo ci)
    {
        if (screen instanceof GuiChest)
        {
            MinecraftForge.EVENT_BUS.post(new ChestOpenEvent());
        }
    }
}
