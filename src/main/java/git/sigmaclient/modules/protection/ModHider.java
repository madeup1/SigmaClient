package git.sigmaclient.modules.protection;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;

public class ModHider extends Module {
    public ModHider()
    {
        super("Mod Hider", Category.PROTECTIONS);
        setToggled(true);
    }

    @Override
    public void assign()
    {
        SigmaClient.modHider = this;
    }
}
