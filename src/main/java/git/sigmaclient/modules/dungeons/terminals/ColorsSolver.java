package git.sigmaclient.modules.dungeons.terminals;

import git.sigmaclient.SigmaClient;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorsSolver extends TerminalSolver
{
    @Override
    public String name()
    {
        return "Colors";
    }

    public static Pattern pattern = Pattern.compile("^Select all the ([\\w ]+) items!$");
    public static String color;
    @Override
    public boolean check(String title)
    {
        Matcher matcher = pattern.matcher(title);
        if (matcher.find())
        {
            color = matcher.group(1).toLowerCase();
            return true;
        }
        return false;
    }

    private static int[] allowedSlots = new int[]
            {
                    10, 11, 12, 13, 14, 15, 16,
                    19, 20, 21, 22, 23, 24, 25,
                    28, 29, 30, 31, 32, 33, 34,
                    37, 38, 39, 40, 41, 42, 43
            };

    private boolean isSlot(int number)
    {
        if ((number >= 10 && number <= 16)
        || (number >= 19 && number <= 25)
        || (number >= 28 && number <= 34)
        || (number >= 37 && number <= 43))
        {
            return true;
        }

        return false;
    }

    private static HashMap<String, String> colorMap = new HashMap<>();
    static
    {
        colorMap.put("light gray dye", "silver");
        colorMap.put("wool", "white");
        colorMap.put("bone meal", "white");
        colorMap.put("ink sac", "black");
        colorMap.put("lapis lazuli", "blue");
        colorMap.put("cocoa beans", "brown");
        colorMap.put("dandelion", "yellow");
        colorMap.put("rose red", "red");
        colorMap.put("cactus green", "green");
    }


    public boolean isCorrect(ItemStack itemStack)
    {
        String lowerName = itemStack.getDisplayName().toLowerCase();

        if (lowerName.startsWith(color))
        {
            return true;
        }

        return colorMap.containsKey(lowerName) && colorMap.get(lowerName).equals(color);
    }

    @Override
    public Slot solve()
    {
        return slots().
                filter(c -> this.isSlot(c.slotNumber)
                        && c.getHasStack()
                        && !c.getStack().isItemEnchanted()
                        && this.isCorrect(c.getStack()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void onClose()
    {
        color = "";
    }
}
