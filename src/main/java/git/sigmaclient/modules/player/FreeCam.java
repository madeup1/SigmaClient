package git.sigmaclient.modules.player;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.events.PacketSentEvent;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.BooleanSetting;
import git.sigmaclient.settings.NumberSetting;
import git.sigmaclient.utils.MovementUtils;
import git.sigmaclient.utils.render.RenderUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FreeCam extends Module
{
    private EntityOtherPlayerMP playerEntity;
    public NumberSetting speed;
    public BooleanSetting tracer;

    public FreeCam() {
        super("FreeCam", Category.PLAYER);
        this.speed = new NumberSetting("Speed", 3.0, 0.1, 5.0, 0.1);
        this.tracer = new BooleanSetting("Show tracer", false);
        this.addSettings(this.speed, this.tracer);
    }

    @Override
    public void assign()
    {
        SigmaClient.freeCam = this;
    }

    @Override
    public void onEnable() {
        if (SigmaClient.mc.theWorld != null) {
            (this.playerEntity = new EntityOtherPlayerMP((World) SigmaClient.mc.theWorld, SigmaClient.mc.thePlayer.getGameProfile())).copyLocationAndAnglesFrom((Entity) SigmaClient.mc.thePlayer);
            this.playerEntity.onGround = SigmaClient.mc.thePlayer.onGround;
            SigmaClient.mc.theWorld.addEntityToWorld(-2137, (Entity)this.playerEntity);
        }
    }

    @Override
    public void onDisable() {
        if (SigmaClient.mc.thePlayer == null || SigmaClient.mc.theWorld == null || this.playerEntity == null) {
            return;
        }
        SigmaClient.mc.thePlayer.noClip = false;
        SigmaClient.mc.thePlayer.setPosition(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ);
        SigmaClient.mc.theWorld.removeEntityFromWorld(-2137);
        this.playerEntity = null;
        SigmaClient.mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
    }

    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (this.isToggled()) {
            SigmaClient.mc.thePlayer.noClip = true;
            SigmaClient.mc.thePlayer.fallDistance = 0.0f;
            SigmaClient.mc.thePlayer.onGround = false;
            SigmaClient.mc.thePlayer.capabilities.isFlying = false;
            SigmaClient.mc.thePlayer.motionY = 0.0;
            if (!MovementUtils.isMoving()) {
                SigmaClient.mc.thePlayer.motionZ = 0.0;
                SigmaClient.mc.thePlayer.motionX = 0.0;
            }
            final double speed = this.speed.getValue() * 0.1;
            SigmaClient.mc.thePlayer.jumpMovementFactor = (float)speed;
            if (SigmaClient.mc.gameSettings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP thePlayer = SigmaClient.mc.thePlayer;
                thePlayer.motionY += speed * 3.0;
            }
            if (SigmaClient.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP thePlayer2 = SigmaClient.mc.thePlayer;
                thePlayer2.motionY -= speed * 3.0;
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (this.isToggled() && this.playerEntity != null && this.tracer.isEnabled()) {
            RenderUtils.tracerLine((Entity)this.playerEntity, event.partialTicks, 1.0f, SigmaClient.clickGui.getColor());
        }
    }

    @SubscribeEvent
    public void onWorldChange(final WorldEvent.Load event) {
        if (this.isToggled()) {
            this.toggle();
        }
    }

    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (this.isToggled() && (event.packet instanceof C03PacketPlayer || event.packet instanceof C09PacketHeldItemChange || event.packet instanceof C08PacketPlayerBlockPlacement || event.packet instanceof C0BPacketEntityAction)) {
            event.setCanceled(true);
        }
    }
}