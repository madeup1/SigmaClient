package git.sigmaclient.modules;

import git.sigmaclient.macro.MacroStatus;

public class Macro extends Module
{
    private MacroStatus status;
    public Macro(String name, int keycode, Category category)
    {
        super(name, keycode, category);
    }

    public Macro(String name, Category category)
    {
        super(name, 0, category);
    }

    public MacroStatus status()
    {
        return this.status;
    }

    public boolean stage(MacroStatus status)
    {
        return this.status == status;
    }

    public boolean renderOverlay()
    {
        return true;
    }
}
