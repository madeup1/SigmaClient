package git.sigmaclient.modules.dungeons.terminals;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.utils.PacketUtils;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RubixSolver extends TerminalSolver
{
    @Override
    public String name()
    {
        return "Rubix";
    }

    public static Pattern pattern = Pattern.compile("Change all to same color!");
    @Override
    public boolean check(String title)
    {
        Matcher matcher = pattern.matcher(title);
        if (matcher.find())
            return true;
        return false;
    }

    private boolean first = true;
    private int desiredColor = 1;

    // yellow red orange blue green

    @Override
    public Slot solve()
    {
        if (first)
        {
            first = false;
            LinkedList<Integer> colors = new LinkedList<>();
            slots()
                    .filter(c -> c.getHasStack()
                            && isSlot(c.slotNumber))
                    .forEach(c ->
                    {
                        int color = this.colorToInt(c.getStack().getItemDamage());

                        colors.addLast(color);
                    });

            int totalClicks = 999;
            ClickResult[] clicks = new ClickResult[9];

            for (int color = 1; color < 6; color++)
            {
                int click = 0;
                ClickResult[] clicksLocal = new ClickResult[9];
                int count = 0;
                for (int color1 : colors)
                {
                    ClickResult result = this.clicksToColor(color1, color);

                    if (result == null)
                        continue;

                    click += result.clicks;
                    clicksLocal[count] = result;

                    count++;
                }

                if (click < totalClicks)
                {
                    totalClicks = click;
                    clicks = clicksLocal;
                    desiredColor = color;
                }
            }

            SigmaClient.debug("Clicks lowest is " + totalClicks);
        }

        List<Slot> slots = slots()
                .filter(
                        c -> c.getHasStack()
                        && isSlot(c.slotNumber)
                        && colorToInt(c.getStack().getItemDamage()) != desiredColor
                )
                .collect(Collectors.toList());

        if (slots.isEmpty())
        {
            return null;
        }

        for (Slot slot : slots)
        {
            ClickResult result = clicksToColor(colorToInt(slot.getStack().getItemDamage()), desiredColor);
            direction = result.direction;

            return slot;
        }

        return null;
    }

    private ClickDirection direction = ClickDirection.RIGHT;

    @Override
    public void click()
    {
        Slot solve = this.solve();

        if (solve == null)
        {
            return;
        }

        transactionId += 1;

        PacketUtils.sendPacket(new C0EPacketClickWindow(windowId, solve.slotNumber, direction == ClickDirection.LEFT ? 0 : 1, 0, solve.getStack(), transactionId));

        lastClick.reset();
        setDelay();
    }

    private static HashMap<Integer, Integer> colorMap = new HashMap<>();

    static
    {
        colorMap.put(13, 1);
        colorMap.put(11, 2);
        colorMap.put(14, 3);
        colorMap.put(1, 4);
        colorMap.put(4, 5);
    }

    public boolean isSlot(int slotNum)
    {
        return (slotNum >= 12 && slotNum <= 14) || (slotNum >= 21 && slotNum <= 23) || (slotNum >= 30 && slotNum <= 32);
    }

    public int colorToInt(int damage)
    {
        return colorMap.get(damage);
    }

    public ClickResult clicksToColor(int colorOriginal, int colorDesired)
    {
        if (colorOriginal == colorDesired)
            return new ClickResult(0, ClickDirection.LEFT);

        int originalL = colorOriginal;
        int originalR = colorOriginal;
        int clicks = 0;

        for (int i = 0; i < 5; i++)
        {
            originalL++;
            originalR--;
            clicks++;

            if (originalL > 5)
            {
                originalL = 1;
            }

            if (originalR < 1)
            {
                originalR = 5;
            }

            if (originalL == colorDesired)
            {
                return new ClickResult(clicks, ClickDirection.LEFT);
            }
            else if (originalR == colorDesired)
            {
                return new ClickResult(clicks, ClickDirection.RIGHT);
            }
        }

        return new ClickResult(0, ClickDirection.LEFT);
    }

    public class ClickResult
    {
        int clicks;
        ClickDirection direction;

        public ClickResult(int clicks, ClickDirection direction)
        {
            this.clicks = clicks;
            this.direction = direction;
        }
    }

    public enum ClickDirection
    {
        LEFT,
        RIGHT
    }

    @Override
    public void onClose()
    {
        first = true;
        desiredColor = 1;
    }
}
