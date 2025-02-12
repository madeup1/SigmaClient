package git.sigmaclient;

import git.sigmaclient.managers.*;
import git.sigmaclient.modules.combat.AntiBot;
import git.sigmaclient.modules.combat.AutoClicker;
import git.sigmaclient.modules.dungeons.AutisTerms;
import git.sigmaclient.modules.misc.BuildGuesser;
import git.sigmaclient.modules.misc.MurderFinder;
import git.sigmaclient.modules.player.*;
import git.sigmaclient.modules.protection.*;
import git.sigmaclient.modules.render.*;
import git.sigmaclient.modules.skyblock.*;
import git.sigmaclient.modules.ClientSettings;
import git.sigmaclient.modules.Module;
import git.sigmaclient.ui.notifications.Notification;
import git.sigmaclient.utils.SkyblockUtils;
import git.sigmaclient.utils.font.Fonts;
import git.sigmaclient.utils.render.shader.BlurUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = SigmaClient.MOD_ID, name = SigmaClient.MOD_NAME, version = SigmaClient.VERSION)
public class SigmaClient
{
    public static final String MOD_ID = "@ID@";
    public static final String MOD_NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    public static final String VERSION_NUMBER = "@VER_NUM@";

    // Managers
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;
    public static ThemeManager themeManager;
    public static NotificationManager notificationManager;

    // Variables
    public static Minecraft mc;
    public static char fancy = (char) 167;

    // Modules

    // System
    public static Gui clickGui;
    public static ClientSettings clientSettings;

    // Render
    public static Animations animations;
    public static ChinaHat chinaHat;
    public static Fullbright fullbright;
    public static Giants giants;
    public static ModernInterfaces modernInterfaces;
    public static InventoryDisplay inventoryDisplay;
    public static TargetDisplay targetDisplay;
    public static Nametags nametags;
    public static PlayerESP playerESP;
    public static ChestESP chestESP;
    public static PopupAnimation popupAnimation;
    public static Trail trail;
    public static Trajectories trajectories;

    // Combat
    public static AntiBot antiBot;
    public static AutoClicker autoClicker;

    // Player
    public static FreeCam freeCam;
    public static FastPlace fastPlace;

    // Movement

    // Skyblock
    public static PurseSpoofer purseSpoofer;

    // Misc
    public static MurderFinder murderFinder;
    public static BuildGuesser buildGuesser;

    // Protections
    public static ModHider modHider;
    public static NickHider nickHider;
    public static StaffAnalyser staffAnalyser;
    public static Proxy proxy;
    public static AntiRat antiRat;

    // Dungeons
    public static AutisTerms autisTerms;

    // misc
    public static List<String> changelog = new ArrayList<>();

    public static void start()
    {
        SigmaClient.mc = Minecraft.getMinecraft();

        moduleManager = new ModuleManager("git.sigmaclient.modules");

        moduleManager.initReflection();
        configManager = new ConfigManager();
        themeManager = new ThemeManager();
        notificationManager = new NotificationManager();

        CommandManager.init();

        changelog.add("What the sigma!");

        loadChangelog();

        for (Module module : moduleManager.modules)
        {
            MinecraftForge.EVENT_BUS.register(module);
        }

        MinecraftForge.EVENT_BUS.register(new SkyblockUtils());

        BlurUtils.registerListener();
    }

    public static void loadChangelog()
    {

    }

    public static void handleKey(int key)
    {
        for (Module module : moduleManager.modules)
        {
            if (module.getKeycode() == key)
            {
                module.toggle();
                if (!clickGui.disableNotifs.isEnabled() && !module.getName().equals("Gui"))
                    notificationManager.showNotification((module.isToggled() ? "Enabled" : "Disabled") + " " + module.getName(), 2000, Notification.NotificationType.INFO);
            }
        }
    }

    @Mod.EventHandler
    public void preInit(FMLInitializationEvent pre) {
        MinecraftForge.EVENT_BUS.register(this);
        Fonts.bootstrap();
        SigmaClient.start();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    public static void sendMessage(Object object)
    {
        if (SigmaClient.mc.thePlayer != null)
        {
            mc.thePlayer.addChatMessage(new ChatComponentText(object.toString()));
        }
    }

    public static void sendMessageWithPrefix(Object object) {
        if (SigmaClient.mc.thePlayer != null)
        {
            SigmaClient.mc.thePlayer.addChatMessage(new ChatComponentText(SigmaClient.fancy + "7[" + SigmaClient.fancy + "q" + SigmaClient.MOD_NAME + SigmaClient.fancy + "r" + SigmaClient.fancy + "7] " + SigmaClient.fancy + "f" + object.toString().replaceAll("&", SigmaClient.fancy + "")));
        }
    }

    public static void debug(Object obj)
    {
        if (clientSettings.debug.isEnabled())
            sendMessageWithPrefix("DEBUG > " + obj);
    }
}
