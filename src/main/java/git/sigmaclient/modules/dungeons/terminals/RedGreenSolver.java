package git.sigmaclient.modules.dungeons.terminals;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.mixins.GuiChestAccessor;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedGreenSolver extends TerminalSolver
{
    @Override
    public String name()
    {
        return "Red Green";
    }

    public static Pattern pattern = Pattern.compile("Correct all the panes!");
    @Override
    public boolean check(String title)
    {
        if (title.equals("Correct all the panes!"))
            return true;
        return false;
    }

    @Override
    public Slot solve()
    {
        return slots()
                .filter(c -> c.getHasStack()
                        && this.isSlot(c.slotNumber)
                        && this.isCorrect(c.getStack()))
                .findFirst()
                .orElse(null);
    }

    public boolean isSlot(int num)
    {
        return (num >= 11 && num <= 15) || (num >= 20 && num <= 24) || (num >= 29 && num <= 33);
    }

    public boolean isCorrect(ItemStack stack)
    {
        return stack.getItemDamage() == 14;
    }

    @Override
    public void onClose()
    {

    }
}
