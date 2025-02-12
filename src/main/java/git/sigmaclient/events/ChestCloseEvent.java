package git.sigmaclient.events;

import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ChestCloseEvent extends Event
{
    public ChestCloseEvent()
    {

    }
}
