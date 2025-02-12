package git.sigmaclient.ui.components;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.settings.SeperatorSetting;
import git.sigmaclient.ui.ModernClickGui;
import git.sigmaclient.utils.font.Fonts;
import git.sigmaclient.utils.render.RenderUtils;

import java.awt.*;

public class CompSeperator extends Comp
{
    private SeperatorSetting setting;
    public CompSeperator(double x, double y, SeperatorSetting setting)
    {
        this.setting = setting;
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawScreen(int x, int y, double scrollY)
    {
        RenderUtils.drawBorderedRoundedRect((float) (ModernClickGui.getX() + x), (float) (ModernClickGui.getY() + y), (float) (ModernClickGui.getWidth() - x - 5), 15, 5, 1, SigmaClient.themeManager.getPrimaryColor().getRGB(), SigmaClient.themeManager.getSecondaryColor().getRGB());
        Fonts.getPrimary().drawCenteredString(setting.name, (float) (ModernClickGui.getX() + x + (ModernClickGui.getWidth() - x)/2), (float) (ModernClickGui.getY() + y + 3), Color.WHITE.getRGB());
    }
}
