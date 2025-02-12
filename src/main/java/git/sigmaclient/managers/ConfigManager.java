package git.sigmaclient.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager
{
    public String configPath;
    public ConfigManager()
    {
        new File(SigmaClient.mc.mcDataDir + "/config/SigmaClient").mkdir();

        File configFile = new File(SigmaClient.mc.mcDataDir + "/config/SigmaClient/SigmaConfig.json");

        if (!configFile.exists())
        {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        configPath = SigmaClient.mc.mcDataDir.getPath() + "/config/SigmaClient/configs/";

        loadConfig();
    }

    public boolean loadConfig(final String configPath) {
        try {
            final String configString = new String(Files.readAllBytes(new File(configPath).toPath()));
            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            final Module[] modules = (Module[])gson.fromJson(configString, (Class)Module[].class);
            for (final Module module : SigmaClient.moduleManager.getModules()) {
                for (final Module configModule : modules) {
                    if (module == null || configModule == null) continue;
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            try {
                                module.setToggled(configModule.isToggled());
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            module.setKeycode(configModule.getKeycode());
                            for (final Setting setting : module.settings) {
                                for (final ConfigSetting cfgSetting : configModule.cfgSettings) {
                                    if (setting != null) {
                                        if (setting.name.equals(cfgSetting.name)) {
                                            if (setting instanceof BooleanSetting) {
                                                ((BooleanSetting)setting).setEnabled((boolean)cfgSetting.value);
                                            }
                                            else if (setting instanceof ModeSetting) {
                                                ((ModeSetting)setting).setSelected((String)cfgSetting.value);
                                            }
                                            else if (setting instanceof NumberSetting) {
                                                ((NumberSetting)setting).setValue((double)cfgSetting.value);
                                            }
                                            else if (setting instanceof StringSetting) {
                                                ((StringSetting)setting).setValue((String)cfgSetting.value);
                                            }
                                        }
                                    }
                                    else {
                                        System.out.println("[Sigma] Setting in " + module.getName() + " is null!");
                                    }
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Config Issue");
                        }
                    }
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }

    public void loadConfig() {
        loadConfig(SigmaClient.mc.mcDataDir + "/config/SigmaClient/SigmaConfig.json");
    }

    public void saveConfig() {
        saveConfig(SigmaClient.mc.mcDataDir + "/config/SigmaClient/SigmaConfig.json", false);
    }

    public boolean saveConfig(final String configPath, final boolean openExplorer) {
        final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        for (final Module module : SigmaClient.moduleManager.getModules()) {
            module.onSave();
            final List<ConfigSetting> settings = new ArrayList<ConfigSetting>();
            for (final Setting setting : module.settings) {
                if (setting == null) continue;
                final ConfigSetting cfgSetting = new ConfigSetting(null, null);
                cfgSetting.name = setting.name;
                if (setting instanceof BooleanSetting) {
                    cfgSetting.value = ((BooleanSetting)setting).isEnabled();
                }
                else if (setting instanceof ModeSetting) {
                    cfgSetting.value = ((ModeSetting)setting).getSelected();
                }
                else if (setting instanceof NumberSetting) {
                    cfgSetting.value = ((NumberSetting)setting).getValue();
                }
                else if (setting instanceof StringSetting) {
                    cfgSetting.value = ((StringSetting)setting).getValue();
                }
                settings.add(cfgSetting);
            }
            module.cfgSettings = settings.toArray(new ConfigSetting[0]);
        }
        try {
            final File file = new File(configPath);
            Files.write(file.toPath(), gson.toJson((Object) SigmaClient.moduleManager.getModules()).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
            if (openExplorer) {
                try {
                    openExplorer();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }

    public void openExplorer() throws IOException {
        Desktop.getDesktop().open(new File(configPath));
    }

    public static class FileSelection implements Transferable
    {
        private List<File> listOfFiles;

        public FileSelection(final List<File> listOfFiles) {
            this.listOfFiles = listOfFiles;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.javaFileListFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            return flavor == DataFlavor.javaFileListFlavor;
        }

        @NotNull
        @Override
        public Object getTransferData(final DataFlavor flavor) {
            return this.listOfFiles;
        }
    }

    public static class ConfigSetting
    {
        @Expose
        @SerializedName("name")
        public String name;
        @Expose
        @SerializedName("value")
        public Object value;

        public ConfigSetting(final String name, final Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
