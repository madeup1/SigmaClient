package git.sigmaclient.modules.dungeons;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.events.ChestCloseEvent;
import git.sigmaclient.events.ChestOpenEvent;
import git.sigmaclient.events.PacketReceivedEvent;
import git.sigmaclient.mixins.GuiChestAccessor;
import git.sigmaclient.modules.Module;
import git.sigmaclient.modules.dungeons.terminals.*;
import git.sigmaclient.settings.BooleanSetting;
import git.sigmaclient.settings.NumberSetting;
import git.sigmaclient.settings.SeperatorSetting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;
import java.util.List;

public class AutisTerms extends Module
{
    public NumberSetting firstDelay = new NumberSetting("First Click Delay", 400, 0, 1000, 10);
    public NumberSetting clickDelay = new NumberSetting("Click Delay", 190, 0, 500, 10);
    public NumberSetting clickRange = new NumberSetting("Click Range", 30, 0, 200, 10);
    public BooleanSetting pip = new BooleanSetting("PIP", true);
    public BooleanSetting clickReject = new BooleanSetting("Click Reject Prot", false);

    public static List<TerminalSolver> solvers = new LinkedList<>();
    static
    {
        solvers.add(new ColorsSolver());
        solvers.add(new MelodySolver());
        solvers.add(new NumberSolver());
        solvers.add(new RedGreenSolver());
        solvers.add(new RubixSolver());
        solvers.add(new StartsWithSolver());
    }

    public AutisTerms()
    {
        super("AutisTerms", Category.DUNGEONS);

        this.addSettings(firstDelay, clickDelay, clickRange, pip);

        solvers.forEach(c -> {
            BooleanSetting setting = new BooleanSetting(c.name(), true);
            c.setSetting(setting);
            this.addSettings("Terminal Toggles", setting);
        });
    }

    public static TerminalSolver activeSolver = null;

    @SubscribeEvent
    public void openChest(ChestOpenEvent event)
    {
        if (!this.isToggled())
        {
            return;
        }

        SigmaClient.debug("Opened chest!");

        if (activeSolver != null)
        {
            activeSolver.close();
            activeSolver = null;
        }

        String inventoryName = ((GuiChestAccessor) SigmaClient.mc.currentScreen).getLowerChestInventory().getDisplayName().getUnformattedText();

        TerminalSolver solver = solvers.stream()
                .filter(c -> c.isToggled()
                        && c.check(inventoryName))
                .findFirst()
                .orElse(null);

        if (solver == null)
        {
            SigmaClient.debug("Failed to find solver for GUI{name='" + inventoryName + "'}");

            return;
        }

        SigmaClient.debug("Solver is " + solver.getClass().getSimpleName());

        activeSolver = solver;
        solver.init();
    }

    @SubscribeEvent
    public void packet(PacketReceivedEvent event)
    {
        if (!this.isToggled())
        {
            return;
        }

        if (event.packet instanceof S2FPacketSetSlot && activeSolver != null)
        {
            activeSolver.slot((S2FPacketSetSlot) event.packet);
        }
    }

    @SubscribeEvent
    public void closeChest(ChestCloseEvent event)
    {
        SigmaClient.debug("Closed chest!");

        if (activeSolver == null)
            return;

        activeSolver.close();
        activeSolver = null;
    }

    @SubscribeEvent
    public void render(RenderGameOverlayEvent event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR && activeSolver != null && this.isToggled())
        {
            if (SigmaClient.mc.currentScreen == null)
            {
                activeSolver.close();
                activeSolver = null;
                return;
            }

            activeSolver.frame();

            if (activeSolver == null)
                return;

            activeSolver.render();
        }
    }

    public static void disableSolver()
    {
        activeSolver = null;
    }

    @Override
    public void assign()
    {
        SigmaClient.autisTerms = this;
    }
}
