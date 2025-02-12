package git.sigmaclient.mixins;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.events.ChestCloseEvent;
import git.sigmaclient.utils.SkyblockUtils;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { NetHandlerPlayClient.class }, priority = 1)
public abstract class MixinPlayHandler
{
    @Shadow
    private Minecraft gameController;
    @Shadow
    private WorldClient clientWorldController;
    @Shadow
    private boolean doneLoadingTerrain;
    @Shadow
    @Final
    private NetworkManager netManager;

    /**
     * @author kwarm
     * @reason Auto terms and ChestCloseEvent
     */
    @Overwrite
    public void handleCloseWindow(S2EPacketCloseWindow windowIn)
    {
        if (MinecraftForge.EVENT_BUS.post(new ChestCloseEvent()))
        {
            return;
        }

        PacketThreadUtil.checkThreadAndEnqueue(windowIn, (NetHandlerPlayClient) ((Object) this), this.gameController);
        this.gameController.thePlayer.closeScreenAndDropStack();
    }
}
