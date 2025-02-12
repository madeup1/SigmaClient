package git.sigmaclient.commands.impl;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.commands.Command;
import git.sigmaclient.utils.PacketUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class PearlCommand extends Command
{
    public PearlCommand()
    {
        super("pearl");
    }
    @Override
    public void execute(String[] args) throws Exception
    {
        ItemStack item = SigmaClient.mc.thePlayer.getHeldItem();

        if (item == null || item.getItem() != Items.ender_pearl)
            return;

        PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(item));
    }

    @Override
    public String getDescription()
    {
        return "";
    }
}
