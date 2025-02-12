package git.sigmaclient.modules.protection;

import com.google.gson.JsonObject;
import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.ModeSetting;
import git.sigmaclient.settings.NumberSetting;
import git.sigmaclient.ui.notifications.Notification;
import git.sigmaclient.utils.MilliTimer;
import git.sigmaclient.utils.Multithreading;
import git.sigmaclient.utils.api.PlanckeScraper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StaffAnalyser extends Module
{
    private ModeSetting mode;
    private MilliTimer timer;
    private int lastBans;

    public StaffAnalyser() {
        super("Staff Analyser", Category.PROTECTIONS);
        this.mode = new ModeSetting("Mode", "FarmHelper", "FarmHelper");
        this.timer = new MilliTimer();
        this.lastBans = -1;
        this.addSettings(this.mode);
    }

    @Override
    public void assign()
    {
        SigmaClient.staffAnalyser = this;
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (!connected)
        {
            this.connect();
        }
    }
    private static boolean connected = false;

    private void connect()
    {

    }
}
