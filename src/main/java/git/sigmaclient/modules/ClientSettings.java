package git.sigmaclient.modules;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.settings.BooleanSetting;
import git.sigmaclient.settings.ModeSetting;
import git.sigmaclient.ui.notifications.Notification;

import org.json.JSONObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.OffsetDateTime;

public class ClientSettings extends Module {
    public ModeSetting hideModules;
    public BooleanSetting debug;
    public BooleanSetting richPresence;
    public BooleanSetting autoUpdate;
    public BooleanSetting cosmeticsUnlocker;

    // Rich Presence

    public static IPCClient ipcClient = new IPCClient(1196540533611450588L);
    private static boolean hasConnected;
    private static boolean shouldConnect = true;
    private static RichPresence richPresenceData;

    // Cosmetics Unlocker

    private boolean unlockerToggle;

    public ClientSettings() {
        super("Client Settings", Category.SETTINGS);
        this.hideModules = new ModeSetting("Hidden modules", "None", "None", "Detected", "Premium");
        this.debug = new BooleanSetting("Developer Mode", false);
        this.richPresence = new BooleanSetting("Rich Presence", true);
        this.autoUpdate = new BooleanSetting("Auto Update", true);
        this.cosmeticsUnlocker = new BooleanSetting("Unlock Cosmetics", true);

        this.addSettings("Client", hideModules, debug, autoUpdate, richPresence, cosmeticsUnlocker);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {

    }

    /*public void disableRichPresence() {
        try {
            ipcClient.close();
            hasConnected = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void setupIPC() {
        if (Minecraft.isRunningOnMac) {
            return;
        }

        try {
            ipcClient.setListener(new IPCListener() {
                @Override
                public void onReady(final IPCClient client) {
                    final RichPresence.Builder builder = new RichPresence.Builder();
                    builder.setStartTimestamp(OffsetDateTime.now());

                    richPresenceData = builder.build();
                    client.sendRichPresence(richPresenceData);

                    hasConnected = true;
                }
            });

            ipcClient.connect(new DiscordBuild[0]);
        }
        catch (Exception e) {
            shouldConnect = false;
            e.printStackTrace();
        }
    }

    @Override
    public void assign() {
        SigmaClient.clientSettings = this;
    }

}