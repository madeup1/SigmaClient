package git.sigmaclient.modules.macros;

import git.sigmaclient.macro.MacroStatus;
import git.sigmaclient.modules.Macro;
import git.sigmaclient.settings.BooleanSetting;

public class AutoMurderMystery extends Macro
{
    public static final MacroStatus LOBBY = new MacroStatus("lobby");
    public static final MacroStatus PRESTART = new MacroStatus("pre-start");

    public BooleanSetting quests = new BooleanSetting("Do Quests", true);

    public AutoMurderMystery()
    {
        super("Auto Murder Mystery", Category.MACROS);

        this.addSettings(quests);
    }
}
