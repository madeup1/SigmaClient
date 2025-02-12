package git.sigmaclient.modules.render;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.BooleanSetting;
import git.sigmaclient.settings.NumberSetting;
import git.sigmaclient.ui.ModernClickGui;
import git.sigmaclient.utils.MilliTimer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PopupAnimation extends Module
{
    public static MilliTimer animationTimer;
    public static MilliTimer lastGuiTimer;
    public static BooleanSetting clickGui;
    public static BooleanSetting inventory;
    public static BooleanSetting chests;
    public static NumberSetting startSize;
    public static NumberSetting time;

    public PopupAnimation() {
        super("Popup Animation", Category.RENDER);
        this.addSettings(PopupAnimation.clickGui, PopupAnimation.inventory, PopupAnimation.chests, PopupAnimation.startSize, PopupAnimation.time);
    }

    @Override
    public void assign()
    {
        SigmaClient.popupAnimation = this;
    }

    public static float getScaling() {
        if (!PopupAnimation.animationTimer.hasTimePassed((long)PopupAnimation.time.getValue())) {
            return (float)(PopupAnimation.animationTimer.getTimePassed() / PopupAnimation.time.getValue() * (1.0 - PopupAnimation.startSize.getValue()) + PopupAnimation.startSize.getValue());
        }
        return 1.0f;
    }

    public static boolean shouldScale(final GuiScreen gui) {
        return SigmaClient.popupAnimation.isToggled() && ((gui instanceof ModernClickGui && PopupAnimation.clickGui.isEnabled()) || (gui instanceof GuiInventory && PopupAnimation.inventory.isEnabled()) || (gui instanceof GuiChest && PopupAnimation.chests.isEnabled()));
    }

    @SubscribeEvent
    public void onTick(final RenderWorldLastEvent event) {
        if (SigmaClient.mc.currentScreen != null) {
            PopupAnimation.lastGuiTimer.reset();
        }
    }

    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
        if (SigmaClient.mc.currentScreen == null && PopupAnimation.lastGuiTimer.hasTimePassed(150L)) {
            PopupAnimation.animationTimer.reset();
        }
    }

    public static void doScaling() {
        final float scaling = getScaling();
        final ScaledResolution res = new ScaledResolution(SigmaClient.mc);
        GL11.glTranslated(res.getScaledWidth() / 2.0, res.getScaledHeight() / 2.0, 0.0);
        GL11.glScaled((double)scaling, (double)scaling, 1.0);
        GL11.glTranslated(-res.getScaledWidth() / 2.0, -res.getScaledHeight() / 2.0, 0.0);
    }

    static {
        PopupAnimation.animationTimer = new MilliTimer();
        PopupAnimation.lastGuiTimer = new MilliTimer();
        PopupAnimation.clickGui = new BooleanSetting("Click Gui", true);
        PopupAnimation.inventory = new BooleanSetting("Inventory", false);
        PopupAnimation.chests = new BooleanSetting("Chests", false);
        PopupAnimation.startSize = new NumberSetting("Starting size", 0.75, 0.01, 1.0, 0.01);
        PopupAnimation.time = new NumberSetting("Time", 200.0, 0.0, 1000.0, 10.0);
    }
}
