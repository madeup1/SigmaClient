package git.sigmaclient.ui.components;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.settings.StringSetting;
import git.sigmaclient.ui.ModernClickGui;
import git.sigmaclient.ui.windows.ModuleWindow;
import git.sigmaclient.utils.font.Fonts;
import git.sigmaclient.utils.render.RenderUtils;

import java.awt.*;

public class CompStringSetting extends Comp {
    public static boolean in = false;
    public StringSetting stringSetting;
    public CompStringSetting(double x, double y, StringSetting stringSetting)
    {
        this.x = x;
        this.y = y;
        this.stringSetting = stringSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, double scrollY)
    {
        if (ModuleWindow.selectedString != null)
        {
            RenderUtils.drawBorderedRoundedRect((float) (ModernClickGui.getX() + x), (float) (ModernClickGui.getY() + y), (float) (ModernClickGui.getWidth() - x - 5), 15, 5, 1, SigmaClient.themeManager.getPrimaryColor().getRGB(), Color.white.getRGB());

            Fonts.getPrimary().drawString(stringSetting.name, ModernClickGui.getX() + x + 5, ModernClickGui.getY() + y + 3, Color.WHITE.getRGB());
            int width = (int) Fonts.getPrimary().getStringWidth(stringSetting.getValue());

            Fonts.getPrimary().drawString(stringSetting.getValue(), ModernClickGui.getX() + ModernClickGui.getWidth() - 10 - width, ModernClickGui.getY() + y + 3, Color.WHITE.getRGB());
        }
        else
        {
            RenderUtils.drawBorderedRoundedRect((float) (ModernClickGui.getX() + x), (float) (ModernClickGui.getY() + y), (float) (ModernClickGui.getWidth() - x - 5), 15, 5, 1, SigmaClient.themeManager.getPrimaryColor().getRGB(), SigmaClient.themeManager.getSecondaryColor().getRGB());

            Fonts.getPrimary().drawString(stringSetting.name, ModernClickGui.getX() + x + 5, ModernClickGui.getY() + y + 3, Color.WHITE.getRGB());
            int width = (int) Fonts.getPrimary().getStringWidth(stringSetting.getValue());

            Fonts.getPrimary().drawString(stringSetting.getValue(), ModernClickGui.getX() + ModernClickGui.getWidth() - 10 - width, ModernClickGui.getY() + y + 3, Color.WHITE.getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (isHovered(mouseX, mouseY, ModernClickGui.getX() + x, ModernClickGui.getY() + y, (ModernClickGui.getWidth() - x - 5), 15))
        {
            if (mouseButton == 0 && ModernClickGui.selectedWindow instanceof ModuleWindow)
            {
                ModuleWindow.selectedString = stringSetting;
                in = true;
            }
        }
        else
        {
            ModuleWindow.selectedString = null;
        }
    }
}
