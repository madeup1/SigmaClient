package git.sigmaclient.mixins;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ PlayerControllerMP.class })
public interface PlayerControllerAccessor
{
    @Accessor
    int getCurrentPlayerItem();

    @Accessor
    void setCurrentPlayerItem(final int p0);
}
