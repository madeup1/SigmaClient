package git.sigmaclient.mixins;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InventoryBasic.class)
public interface InventoryBasicAccessor
{
    @Accessor
    ItemStack[] getInventoryContents();
}
