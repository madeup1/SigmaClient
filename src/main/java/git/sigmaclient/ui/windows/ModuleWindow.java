package git.sigmaclient.ui.windows;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.*;
import git.sigmaclient.ui.components.*;
import git.sigmaclient.ui.ModernClickGui;
import git.sigmaclient.utils.AnimationUtils;
import git.sigmaclient.utils.MouseUtils;
import git.sigmaclient.utils.StencilUtils;
import git.sigmaclient.utils.font.Fonts;
import git.sigmaclient.utils.render.RenderUtils;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.client.gui.GuiScreen.getClipboardString;

public class ModuleWindow extends Window {
    public static AnimationUtils scrollAnimation = new AnimationUtils(0.0);
    public static double scrollY;
    public static double scrollYsettings;
    public List<Module> modulesInCategory;
    public static String lastSettingCategory = "";
    public static Module selectedModule;
    public static StringSetting selectedString = null;
    public static NumberSetting selectedNumber = null;
    public static Module changeBind = null;
    public static AnimationUtils settingsAnimation = new AnimationUtils(0.0);

    public ModuleWindow(Module.Category moduleCategory) {
        super(moduleCategory.name);
        this.modulesInCategory = Module.getModulesByCategory(moduleCategory).stream().sorted(Comparator.comparingDouble(c -> Fonts.getPrimary().getStringWidth(c.getName()))).collect(Collectors.toList());
        Collections.reverse(this.modulesInCategory);
    }

    @Override
    public void initGui() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        MouseUtils.Scroll scroll;
        int offset = 30;
        if (!ModernClickGui.settingsOpened) {
            for (Module module : this.modulesInCategory) {
                if(!ModernClickGui.searchValue.isEmpty() && !module.getName().toLowerCase().contains(ModernClickGui.searchValue.toLowerCase())) {
                    continue;
                }

                if (module.getFlagType() == Module.FlagType.DETECTED)
                {
                    if(SigmaClient.clientSettings.hideModules.getSelected().equals("Detected") || SigmaClient.clientSettings.hideModules.getSelected().equals("Premium + Detected")) {
                        continue;
                    }
                }

                if (module.getVersionType() == Module.VersionType.PREMIUM)
                {
                    if(SigmaClient.clientSettings.hideModules.getSelected().equals("Premium") || SigmaClient.clientSettings.hideModules.getSelected().equals("Premium + Detected")) {
                        continue;
                    }
                }

                RenderUtils.drawBorderedRoundedRect((float)(ModernClickGui.getX() + 95.0), (float)(ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue()), ModernClickGui.getWidth() - 100.0f, 20.0f, 3.0f, 1.0f, module.isToggled() ? SigmaClient.themeManager.getSecondaryColor().getRGB() : SigmaClient.themeManager.getPrimaryColor().getRGB(), SigmaClient.themeManager.getSecondaryColor().getRGB());

                int shift = 105;

                if (module.getFlagType() == Module.FlagType.DETECTED)
                {
                    shift += 10;

                    Fonts.newIcons.drawString("A", ModernClickGui.getX() + 100, ModernClickGui.getY() + (double) offset + this.scrollAnimation.getValue() + 6.0, Color.YELLOW.getRGB());
                }

                if (module.getVersionType() == Module.VersionType.PREMIUM)
                {
                    shift += 10;

                    Fonts.newIcons.drawString("n", ModernClickGui.getX() + 100, ModernClickGui.getY() + (double) offset + this.scrollAnimation.getValue() + 6.0, Color.BLACK.getRGB());
                }

                Fonts.getPrimary().drawString(module.getName(), ModernClickGui.getX() + shift, ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue() + 7.0, Color.WHITE.getRGB());
                if (!module.getSettings().isEmpty()) {
                    Fonts.icon.drawString("C", ModernClickGui.getX() + (double)ModernClickGui.getWidth() - 21.0, ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue() + 8.0, Color.WHITE.getRGB());
                }

                if (changeBind == module)
                {
                    String keyname = "[...]";
                    int length = (int) Fonts.getPrimary().getStringWidth(keyname);
                    Fonts.getPrimary().drawString(keyname, ModernClickGui.getX() + (double)ModernClickGui.getWidth() - 30 - length, ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue() + 7.0, Color.WHITE.getRGB());
                }
                else if (module.getKeycode() != 0)
                {
                    String keyname = "[" + ((module.getKeycode() >= 256) ? "  " : Keyboard.getKeyName(module.getKeycode()).replaceAll("NONE", "  ")) + "]";
                    int length = (int) Fonts.getPrimary().getStringWidth(keyname);
                    Fonts.getPrimary().drawString(keyname, ModernClickGui.getX() + (double)ModernClickGui.getWidth() - 30 - length, ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue() + 7.0, Color.WHITE.getRGB());
                }
                else
                {
                    String keyname = "[NONE]";
                    int length = (int) Fonts.getPrimary().getStringWidth(keyname);
                    Fonts.getPrimary().drawString(keyname, ModernClickGui.getX() + (double)ModernClickGui.getWidth() - 30 - length, ModernClickGui.getY() + (double)offset + this.scrollAnimation.getValue() + 7.0, Color.WHITE.getRGB());
                }
                offset += 25;
            }
        } else if (selectedModule != null) {
            //Fonts.getPrimary().drawString(selectedModule.getName(), ModernClickGui.getX() + 95.0, 30.0, Color.WHITE.getRGB());

            for (Comp comp : updateComps(selectedModule.getSettings())) {
                comp.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
        scroll = MouseUtils.scroll();
        if (scroll != null && !ModernClickGui.settingsOpened) {
            switch (scroll) {
                case DOWN: {
                    if (!(scrollY > (double)(-(this.modulesInCategory.size() - 8) * 25))) break;
                    scrollY -= 25.0;
                    break;
                }
                case UP: {
                    if (scrollY < -10.0) {
                        scrollY += 25.0;
                        break;
                    }
                    if (this.modulesInCategory.size() <= 8) break;
                    scrollY = 0.0;
                }
            }
        }
        else if (scroll != null && ModernClickGui.settingsOpened)
        {
            switch (scroll)
            {
                case DOWN: {
                    if ((scrollYsettings > (ModernClickGui.getHeight()) - settingsHeight))
                        scrollYsettings -= 10;
                    break;
                }
                case UP: {
                    scrollYsettings += 10;
                    if (scrollYsettings >= 0)
                    {
                        scrollYsettings = 0;
                    }
                    if (settingsHeight < (ModernClickGui.getHeight() - 25))
                        scrollYsettings = 0.0;
                }
            }
        }
        this.scrollAnimation.setAnimation(scrollY, 16.0);
        settingsAnimation.setAnimation(scrollYsettings, 16);
        StencilUtils.disableStencilBuffer();
    }

    public static List<Comp> updateComps(List<Setting> settings) {
        List<Comp> comps = new ArrayList<>();
        int settingOffset = 30 + (int) settingsAnimation.getValue();
        boolean lastBool = false;
        for (Setting s : settings) {
            if (s.isHidden()) continue;

            if (!(s instanceof BooleanSetting) && lastBool) {
                lastBool = false;
                settingOffset += 15;
            }

            if(!lastSettingCategory.equals(s.category)) {
                lastSettingCategory = s.category;
                if(!lastSettingCategory.isEmpty()) {
                    if (s instanceof BooleanSetting && lastBool)
                    {
                        lastBool = false;
                        settingOffset += 15;
                        comps.add(new CompSettingSpacer(95.0, settingOffset, s.category));
                        settingOffset += 15;
                    } else if(lastBool) {
                        lastBool = false;
                        settingOffset += 15;
                        comps.add(new CompSettingSpacer(95.0, settingOffset, s.category));
                    } else {
                        comps.add(new CompSettingSpacer(95.0, settingOffset, s.category));
                        settingOffset += 15;
                    }
                }
            }

            if (s instanceof BooleanSetting && !lastBool) {
                comps.add(new CompBoolSetting(95.0, settingOffset, (BooleanSetting)s));
                lastBool = true;
            }
            else if (s instanceof BooleanSetting && lastBool)
            {
                comps.add(new CompBoolSetting(95.0f + (ModernClickGui.getWidth() - 95.0f) / 2.0f, settingOffset, (BooleanSetting)s));
                settingOffset += 15;
                lastBool = false;
            }
            else if (s instanceof ModeSetting)
            {
                comps.add(new CompModeSetting(95, settingOffset, (ModeSetting) s));
                settingOffset += 20;
            }
            else if (s instanceof StringSetting)
            {
                comps.add(new CompStringSetting(95, settingOffset, (StringSetting) s));
                settingOffset += 20;
            }
            else if (s instanceof RunnableSetting)
            {
                comps.add(new CompRunnableSetting(95, settingOffset, (RunnableSetting) s));
                settingOffset += 20;
            }
            else if (s instanceof NumberSetting)
            {
                comps.add(new CompSliderSetting(95, settingOffset, (NumberSetting) s));
                settingOffset += 25;
            }
            else if (s instanceof SeperatorSetting)
            {
                comps.add(new CompSeperator(95, settingOffset, (SeperatorSetting) s));
                settingOffset += 20;
            }
        }

        settingsHeight = settingOffset - (int) (settingsAnimation.getValue());
        return comps;
    }

    public static int settingsHeight;
    public void close()
    {
        selectedModule = null;
        settingsHeight = 0;
        scrollYsettings = 0;
        settingsAnimation.setAnimation(0, 0);
        ModernClickGui.settingsOpened = false;
        selectedString = null;
        selectedNumber = null;
        changeBind = null;
        SigmaClient.configManager.saveConfig();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int offset = 30;
        if (selectedModule == null) {
            for (Module module : this.modulesInCategory) {
                if(!ModernClickGui.searchValue.isEmpty() && !module.getName().toLowerCase().contains(ModernClickGui.searchValue.toLowerCase())) {
                    continue;
                }
                
                if (this.isHovered(mouseX, mouseY, ModernClickGui.getX() + 95.0, ModernClickGui.getY() + (double) offset + this.scrollAnimation.getValue(), ModernClickGui.getWidth() - 100.0f, 20.0)) {
                    switch (mouseButton) {
                        case 0: {
                            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                            {
                                changeBind = module;
                                return;
                            }
                            else
                            {
                                module.toggle();
                            }
                            break;
                        }
                        case 1: {
                            if (module.getSettings().isEmpty()) {
                                return;
                            }
                            else {
                                close();
                                selectedModule = module;
                                ModernClickGui.settingsOpened = true;
                            }
                            break;
                        }
                        case 2: {
                            changeBind = module;
                            break;
                        }
                    }
                }
                offset += 25;
            }
        }
        else
        {
            for (Comp comp : updateComps(selectedModule.getSettings()))
            {
                if (CompStringSetting.in)
                {
                    CompStringSetting.in = false;
                    break;
                }
                comp.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        changeBind = null;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        SigmaClient.configManager.saveConfig();
        if (selectedModule == null) return;

        for (Comp comp : updateComps(selectedModule.getSettings()))
        {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }

        if (selectedNumber != null)
        {
            selectedNumber = null;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == SigmaClient.clickGui.getKeycode())
        {
            if (changeBind != null)
            {
                changeBind.setKeycode(0);
                changeBind = null;
            }
        }
        else if (keyCode == Keyboard.KEY_LSHIFT)
        {
            if (changeBind != null) return;
        }

        if (changeBind != null && keyCode != 1)
        {
            changeBind.setKeycode(keyCode);
            changeBind = null;
        }
        if (selectedModule == null) return;
        if (selectedString == null) return;

        else if (selectedString != null) {
            if (keyCode == 28) {
                selectedString = null;
            }
            else if (keyCode == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
                selectedString.setValue(this.selectedString.getValue() + getClipboardString());
            }
            else if (keyCode != 14) {
                selectedString.setValue(ChatAllowedCharacters.filterAllowedCharacters(this.selectedString.getValue() + typedChar));
            }
            else {
                selectedString.setValue(this.selectedString.getValue().substring(0, Math.max(0, this.selectedString.getValue().length() - 1)));
            }
        }

        SigmaClient.configManager.saveConfig();
    }

    static {
        selectedModule = null;
    }
}
