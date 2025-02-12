package git.sigmaclient.modules.dungeons.terminals;

import git.sigmaclient.SigmaClient;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NumberSolver extends TerminalSolver
{
    @Override
    public String name()
    {
        return "Numbers";
    }

    public static Pattern pattern = Pattern.compile("Click in order!");
    @Override
    public boolean check(String title)
    {
        return title.equals("Click in order!");
    }

    @Override
    public Slot solve()
    {
        Set<Slot> slots = slots()
                .filter(c -> this.isSlot(c.slotNumber)
                && c.getHasStack()
                && this.isCorrect(c.getStack())).collect(Collectors.toSet());

        int index = 14;
        Slot ref = null;
        for (Slot slot : slots)
        {
            if (slot.getStack().stackSize <= index)
            {
                ref = slot;
                index = slot.getStack().stackSize;
            }
        }

        return ref;
    }

    public boolean isSlot(int num)
    {
        return (num >= 10 && num <= 16)
                || (num >= 19 && num <= 25);
    }

    public boolean isCorrect(ItemStack stack)
    {
        if (stack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane)
        && stack.getItemDamage() == 14)
            return true;
        return false;
    }

    @Override
    public void onClose()
    {

    }
}
