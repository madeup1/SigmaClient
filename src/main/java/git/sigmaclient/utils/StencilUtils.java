package git.sigmaclient.utils;

import git.sigmaclient.SigmaClient;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class StencilUtils
{
    public static void checkSetupFBO(final Framebuffer framebuffer) {
        if (framebuffer != null && framebuffer.depthBuffer > -1) {
            setupFBO(framebuffer);
            framebuffer.depthBuffer = -1;
        }
    }

    public static void enableStencilBuffer() {
        SigmaClient.mc.getFramebuffer().bindFramebuffer(false);
        if (SigmaClient.mc.getFramebuffer() != null) {
            if (SigmaClient.mc.getFramebuffer().depthBuffer > -1) {
                setupFBO(SigmaClient.mc.getFramebuffer());
                SigmaClient.mc.getFramebuffer().depthBuffer = -1;
            }
        }
        glClear(GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);

        glStencilFunc(GL_ALWAYS, 1, 1);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
        glColorMask(false, false, false, false);
    }

    public static void readStencilBuffer(int ref) {
        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, ref, 1);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
    }

    public static void disableStencilBuffer() {
        glDisable(GL_STENCIL_TEST);
    }

    public static void setupFBO(final Framebuffer framebuffer) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.depthBuffer);
        final int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencilDepthBufferID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, SigmaClient.mc.displayWidth, SigmaClient.mc.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferID);
    }

    public static void initStencil() {
        initStencil(SigmaClient.mc.getFramebuffer());
    }

    public static void initStencil(final Framebuffer framebuffer) {
        framebuffer.bindFramebuffer(false);
        checkSetupFBO(framebuffer);
        glClear(1024);
        glEnable(2960);
    }

    public static void bindWriteStencilBuffer() {
        GL11.glStencilFunc(519, 1, 1);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glColorMask(false, false, false, false);
    }

    public static void bindReadStencilBuffer(final int ref) {
        GL11.glColorMask(true, true, true, true);
        GL11.glStencilFunc(514, ref, 1);
        GL11.glStencilOp(7680, 7680, 7680);
    }

    public static void uninitStencil() {
        GL11.glDisable(2960);
    }
}