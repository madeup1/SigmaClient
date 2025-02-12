package git.sigmaclient.modules.render;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.events.GuiChatEvent;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.ModeSetting;
import git.sigmaclient.settings.NumberSetting;
import git.sigmaclient.ui.hud.DraggableComponent;
import git.sigmaclient.ui.hud.impl.InventoryHud;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InventoryDisplay extends Module
{
    public static ModeSetting defaultPosition;
    public NumberSetting x;
    public NumberSetting y;
    public ModeSetting blurStrength;

    public InventoryDisplay() {
        super("Inventory Display", 0, Module.Category.RENDER);
        defaultPosition = new ModeSetting("Default Position", "Top Left", new String[] { "Top Left", "Top Right", "Bottom Left", "Bottom Right", "Custom"});
        this.x = new NumberSetting("customX", 0.0, -100000.0, 100000.0, 1.0E-5, a -> true);
        this.y = new NumberSetting("customY", 0.0, -100000.0, 100000.0, 1.0E-5, a -> true);
        this.blurStrength = new ModeSetting("Blur Strength", "Low", new String[] { "None", "Low", "High" });
        this.addSettings(defaultPosition, this.x, this.y, this.blurStrength);
    }

    @Override
    public void assign()
    {
        SigmaClient.inventoryDisplay = this;
    }

    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (this.isToggled() && event.type.equals((Object)RenderGameOverlayEvent.ElementType.HOTBAR) && SigmaClient.mc.thePlayer != null) {
            InventoryHud.inventoryHUD.drawScreen();
        }
    }

    @SubscribeEvent
    public void onChatEvent(final GuiChatEvent event) {
        if (!this.isToggled()) {
            return;
        }

        final DraggableComponent component = InventoryHud.inventoryHUD;

        if (event instanceof GuiChatEvent.MouseClicked) {
            if (component.isHovered(event.mouseX, event.mouseY)) {
                defaultPosition.setSelected("Custom");
                component.startDragging();
            }
        }
        else if (event instanceof GuiChatEvent.MouseReleased) {
            component.stopDragging();
        }
        else if (event instanceof GuiChatEvent.Closed) {
            component.stopDragging();
        }
        else if (event instanceof GuiChatEvent.DrawChatEvent) {

        }
    }
}