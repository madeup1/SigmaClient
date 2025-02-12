package git.sigmaclient.modules.dungeons.terminals;

import git.sigmaclient.SigmaClient;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartsWithSolver extends TerminalSolver
{
    @Override
    public String name()
    {
        return "Starts With";
    }

    public static Pattern pattern = Pattern.compile("^What starts with: '(\\w)'\\?$");
    private static String start = "";
    @Override
    public boolean check(String title)
    {
        Matcher matcher = pattern.matcher(title);
        if (matcher.find())
        {
            start = matcher.group(1).toLowerCase();

            SigmaClient.debug("Start is " + start);

            return true;
        }
        return false;
    }

    @Override
    public Slot solve()
    {
        Container container = SigmaClient.mc.thePlayer.openContainer;

        return slots()
                .filter(c -> this.isSlot(c.slotNumber)
                        && c.getHasStack()
                        && !c.getStack().isItemEnchanted()
                        && this.isCorrect(c.getStack()))
                .findFirst()
                .orElse(null);
    }

    public boolean isCorrect(ItemStack stack)
    {
        SigmaClient.debug("Display name is " + stack.getDisplayName());

        return stack.getDisplayName().toLowerCase().startsWith(start);
    }

    public boolean isSlot(int num)
    {
        return (num >= 10 && num <= 16) || (num >= 19 && num <= 25) || (num >= 28 && num <= 34);
    }

    @Override
    public void onClose()
    {
        start = "";
    }
}
