package git.sigmaclient.settings;

import java.util.function.Predicate;

public class SeperatorSetting extends Setting
{
    public SeperatorSetting(String name, Predicate<Boolean> predicate)
    {
        super(name, predicate);
    }
}
