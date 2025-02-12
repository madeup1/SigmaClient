package git.sigmaclient.modules.render;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.BooleanSetting;
import git.sigmaclient.settings.NumberSetting;

public class Giants extends Module
{
    public NumberSetting scale;
    public BooleanSetting mobs;
    public BooleanSetting players;
    public BooleanSetting armorStands;

    public Giants() {
        super("Giants", Category.RENDER);
        this.scale = new NumberSetting("Scale", 2.0, 0.1, 5.0, 0.1);
        this.mobs = new BooleanSetting("Mobs", false);
        this.players = new BooleanSetting("Players", true);
        this.armorStands = new BooleanSetting("ArmorStands", false);
        this.addSettings(this.scale, this.players, this.mobs, this.armorStands);
    }

    @Override
    public void assign()
    {
        SigmaClient.giants = this;
    }
}
