package git.sigmaclient.modules.protection;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.StringSetting;

public class NickHider extends Module {
    public StringSetting nick;
    public NickHider()
    {
        super("Nick Hider", Category.PROTECTIONS);
        this.nick = new StringSetting("Name", SigmaClient.mc.getSession().getUsername());
        addSettings(nick);
    }

    @Override
    public void assign()
    {
        SigmaClient.nickHider = this;
    }

    @Override
    public String suffix()
    {
        return nick.getValue();
    }
}
