package git.sigmaclient.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SessionAccessEvent
{
    private final StackTraceElement[] elements;
    public SessionAccessEvent(StackTraceElement[] elements)
    {
        this.elements = elements;
    }
    public StackTraceElement[] elements()
    {
        return this.elements;
    }
}
