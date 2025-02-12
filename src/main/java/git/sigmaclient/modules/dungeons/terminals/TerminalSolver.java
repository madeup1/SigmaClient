package git.sigmaclient.modules.dungeons.terminals;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.mixins.GuiChestAccessor;
import git.sigmaclient.mixins.InventoryBasicAccessor;
import git.sigmaclient.modules.dungeons.AutisTerms;
import git.sigmaclient.settings.BooleanSetting;
import git.sigmaclient.utils.MathUtils;
import git.sigmaclient.utils.MilliTimer;
import git.sigmaclient.utils.PacketUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;

import java.util.stream.Stream;

public abstract class TerminalSolver
{
    public abstract String name();
    public abstract boolean check(String title);
    public abstract Slot solve(); // next slot to click :sigma:
    public abstract void onClose();

    public static MilliTimer lastClick;
    private static long nextDelay = -1;
    public static int windowId = -1;
    public static short transactionId = 0;

    public TerminalSolver()
    {
        lastClick = new MilliTimer();
    }

    public void render()
    {

    }

    public void init()
    {
        double extra = SigmaClient.autisTerms.clickRange.getValue();
        double delay = SigmaClient.autisTerms.firstDelay.getValue();

        nextDelay = (long) (MathUtils.getRandomInRange(extra, 0) + delay);

        lastClick.reset();
    }

    public void setDelay()
    {
        double extra = SigmaClient.autisTerms.clickRange.getValue();
        double delay = SigmaClient.autisTerms.clickDelay.getValue();

        nextDelay = (long) (MathUtils.getRandomInRange(extra, 0) + delay);
    }

    private BooleanSetting setting;
    public void setSetting(BooleanSetting setting)
    {
        this.setting = setting;
    }

    public boolean isToggled()
    {
        return this.setting.isEnabled();
    }

    public void click()
    {
        Slot solve = this.solve();

        if (solve == null)
        {
            return;
        }

        transactionId += 1;

        PacketUtils.sendPacket(new C0EPacketClickWindow(windowId, solve.slotNumber, 0, 0, solve.getStack(), transactionId));
        willClickReject = true;

        SigmaClient.debug("Clicked " + solve.slotNumber + "!");

        lastClick.reset();
        setDelay();
    }

    public void frame()
    {
        if (lastClick.hasTimePassed(nextDelay))
        {
            // SigmaClient.sendMessageWithPrefix("&fClick term funneh");

            this.click();
        }
    }

    private IInventory inventory;

    public void setInventory(IInventory inventory)
    {
        this.inventory = inventory;
    }

    public IInventory inventory()
    {
        return this.inventory;
    }

    public Stream<Slot> slots()
    {
        return ((GuiChest) SigmaClient.mc.currentScreen).inventorySlots.inventorySlots.stream();
    }

    public void close()
    {
        lastClick.setTime(0);
        windowId = -1;
        nextDelay = -1;
        transactionId = 0;
        inventory = null;

        this.onClose();
    }

    public void setWindowId(int id)
    {
        windowId = id;
    }

    public boolean willClickReject = false;

    public void slot(S2FPacketSetSlot slot)
    {
        willClickReject = false;
    }
}
