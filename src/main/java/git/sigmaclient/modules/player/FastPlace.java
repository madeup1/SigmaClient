package git.sigmaclient.modules.player;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.NumberSetting;

public class FastPlace extends Module {
    public NumberSetting placeDelay;
    public FastPlace()
    {
        super("Fast Place", Category.PLAYER);
        this.addSettings(this.placeDelay = new NumberSetting("Place delay", 2.0, 0.0, 4.0, 1.0));
        this.setFlagType(FlagType.DETECTED);
    }

    @Override
    public void assign()
    {
        SigmaClient.fastPlace = this;
    }
}