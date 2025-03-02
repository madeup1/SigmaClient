package git.sigmaclient.modules.render;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.NumberSetting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemBow;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Trajectories extends Module {

    public NumberSetting width;

    public Trajectories() {
        super("Trajectories", Category.RENDER);
        this.width = new NumberSetting("Thickness", 2, 1, 10, 1);
        this.addSettings(this.width);
    }

    @Override
    public void assign()
    {
        SigmaClient.trajectories = this;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent e) {
        if (!this.isToggled() || SigmaClient.mc.thePlayer == null || SigmaClient.mc.theWorld == null) return;
        if (!(SigmaClient.mc.thePlayer.isUsingItem() && SigmaClient.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow)) return;

        EntityPlayerSP player = SigmaClient.mc.thePlayer;

        Timer timer = new Timer(3F);
        double arrowPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * timer.renderPartialTicks
                - Math.cos((float) Math.toRadians(player.rotationYaw)) * 0.16F;
        double arrowPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * timer.renderPartialTicks
                + player.getEyeHeight() - 0.1;
        double arrowPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * timer.renderPartialTicks
                - Math.sin((float) Math.toRadians(player.rotationYaw)) * 0.16F;

        float arrowMotionFactor = 1F;
        float yaw = (float) Math.toRadians(player.rotationYaw);
        float pitch = (float) Math.toRadians(player.rotationPitch);
        float arrowMotionX = (float) (-Math.sin(yaw) * Math.cos(pitch) * arrowMotionFactor);
        float arrowMotionY = (float) (-Math.sin(pitch) * arrowMotionFactor);
        float arrowMotionZ = (float) (Math.cos(yaw) * Math.cos(pitch) * arrowMotionFactor);
        double arrowMotion = Math
                .sqrt(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
        arrowMotionX /= (float) arrowMotion;
        arrowMotionY /= (float) arrowMotion;
        arrowMotionZ /= (float) arrowMotion;
        float bowPower = (72000 - player.getItemInUseCount()) / 20F;
        bowPower = (bowPower * bowPower + bowPower * 2F) / 3F;

        if (bowPower > 1F || bowPower <= 0.1F)
            bowPower = 1F;

        bowPower *= 3F;
        arrowMotionX *= bowPower;
        arrowMotionY *= bowPower;
        arrowMotionZ *= bowPower;

        // GL settings
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth((int) width.getValue());

        RenderManager renderManager = SigmaClient.mc.getRenderManager();

        double gravity = 0.05D;
        Vec3 playerVector = new Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        final Color color = SigmaClient.clickGui.getColor();
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.75f);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (int i = 0; i < 1000; i++) {
            GL11.glVertex3d(arrowPosX - renderManager.viewerPosX, arrowPosY - renderManager.viewerPosY,
                    arrowPosZ - renderManager.viewerPosZ);

            arrowPosX += arrowMotionX * 0.1;
            arrowPosY += arrowMotionY * 0.1;
            arrowPosZ += arrowMotionZ * 0.1;
            arrowMotionX *= 0.999F;
            arrowMotionY *= 0.999F;
            arrowMotionZ *= 0.999F;
            arrowMotionY -= (float) (gravity * 0.1);

            if (SigmaClient.mc.theWorld.rayTraceBlocks(playerVector, new Vec3(arrowPosX, arrowPosY, arrowPosZ)) != null)
                break;
        }
        GL11.glEnd();

        double renderX = arrowPosX - renderManager.viewerPosX;
        double renderY = arrowPosY - renderManager.viewerPosY;
        double renderZ = arrowPosZ - renderManager.viewerPosZ;

        GL11.glPushMatrix();
        GL11.glTranslated(renderX - 0.5, renderY - 0.5, renderZ - 0.5);

        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.25f);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.75f);

        GL11.glPopMatrix();

        // GL resets
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();

    }
}