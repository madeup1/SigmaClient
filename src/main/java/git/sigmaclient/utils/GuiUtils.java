package git.sigmaclient.utils;

import git.sigmaclient.SigmaClient;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;

public class GuiUtils {
    public static String getInventoryName(GuiScreen gui) {
        if (gui instanceof GuiChest) {
            return ((ContainerChest) ((GuiChest) gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        } else return "";
    }

    public static String getOpenInventoryName() {
        if (SigmaClient.mc.thePlayer == null || SigmaClient.mc.theWorld == null) {
            return null;
        } else return SigmaClient.mc.thePlayer.openContainer.inventorySlots.get(0).inventory.getName();
    }
}
