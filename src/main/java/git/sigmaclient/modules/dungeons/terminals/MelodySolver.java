package git.sigmaclient.modules.dungeons.terminals;

import git.sigmaclient.SigmaClient;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MelodySolver extends TerminalSolver
{
    @Override
    public String name()
    {
        return "Melody";
    }

    public static Pattern pattern = Pattern.compile("Click the button on time!");
    @Override
    public boolean check(String title)
    {
        Matcher matcher = pattern.matcher(title);
        if (matcher.find())
            return true;
        return false;
    }

    private int stage = 0;

    @Override
    public Slot solve()
    {
        List<Slot> slots = slots().collect(Collectors.toList());

        int column = 0;

        for (int i = 1; i <= 5; i++)
        {
            if (slots.get(i).getStack().getItemDamage() == 2)
            {
                column = i;
                break;
            }
        }

        if (column == 0)
        {
            SigmaClient.debug("Couldn't find column???");

            return null;
        }

        if (slots.get(column + 9 + (9 * stage)).getStack().getItemDamage() == 5)
        {
            Slot slot = slots.get(16 + (9 * stage));

            stage++;

            return slot;
        }

        return null;
    }

    @Override
    public void onClose()
    {
        stage = 0;
    }
}
