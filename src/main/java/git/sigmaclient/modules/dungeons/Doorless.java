package git.sigmaclient.modules.dungeons;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.events.PacketReceivedEvent;
import git.sigmaclient.modules.Module;
import git.sigmaclient.utils.rotation.RotationUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Doorless extends Module
{
    public Doorless()
    {
        super("Doorless", Category.DUNGEONS);
    }

    private boolean inDoor = false;

    @SubscribeEvent
    public void render(TickEvent.ClientTickEvent event)
    {
        if (!this.isToggled() || SigmaClient.mc.thePlayer == null || SigmaClient.mc.theWorld == null || event.phase == TickEvent.Phase.END)
        {
            return;
        }

        /*float yaw = SigmaClient.mc.thePlayer.rotationYaw;
        float pitch = SigmaClient.mc.thePlayer.rotationPitch;

        EnumFacing facing = SigmaClient.mc.thePlayer.getHorizontalFacing();

        switch (facing)
        {
            case EAST:
            case WEST:
                this.eastWestClip();
                break;
            case NORTH:
            case SOUTH:
                this.northSouthClip();
        }*/
    }

    public void eastWestClip()
    {
        SigmaClient.debug("East-West clipping!");

        BlockPos check = SigmaClient.mc.thePlayer.getPosition();
        check.add(0, 0, -1);

        IBlockState state = SigmaClient.mc.theWorld.getBlockState(check);

        BlockPos skullCheck = SigmaClient.mc.thePlayer.getPosition();
        skullCheck.add(-1, 1, 0);

        TileEntity skullState = SigmaClient.mc.theWorld.getTileEntity(skullCheck);
        if (skullState == null)
        {
            return;
        }
        if (skullState.getBlockType() != Blocks.skull)
        {
            SigmaClient.debug("failed skull check");

            return;
        }

        BlockPos airSet;
        int dir = 0;

        if (state.getBlock() == Blocks.coal_block)
        {
            airSet = SigmaClient.mc.thePlayer.playerLocation.add(-3, 1, 0);
            dir = 1;
        }
        else
        {
            airSet = SigmaClient.mc.thePlayer.playerLocation.add(3, 1, 0);
            dir = -1;
        }

        SigmaClient.debug("Setting air");

        for (int z = 0; z < 5; z++)
        {
            for (int y = 0; y < 3; y++)
            {
                SigmaClient.mc.theWorld.setBlockToAir(new BlockPos(airSet.add(0, y, z * dir)));
            }
        }
    }

    public void northSouthClip()
    {

    }
}
