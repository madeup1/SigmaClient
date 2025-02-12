package git.sigmaclient.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import git.sigmaclient.SigmaClient;
import git.sigmaclient.mixins.PlayerControllerAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PlayerUtils
{
    public static boolean lastGround;

    public static Method clickMouse;

    private PlayerUtils() {
    }

    public static boolean isNPC(final Entity entity) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        }
        final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
        return ChatFormatting.stripFormatting(entity.getDisplayName().getUnformattedText()).startsWith("[NPC]") || (entity.getUniqueID().version() == 2 && entityLivingBase.getHealth() == 20.0f && entityLivingBase.getMaxHealth() == 20.0f);
    }

    public static void click()
    {
        SigmaClient.mc.thePlayer.swingItem();

        if (SigmaClient.mc.objectMouseOver == null)
        {
            return;
        }

        switch (SigmaClient.mc.objectMouseOver.typeOfHit)
        {
            case ENTITY:
                SigmaClient.mc.playerController.attackEntity(SigmaClient.mc.thePlayer, SigmaClient.mc.objectMouseOver.entityHit);
                break;
        }
    }

    private static Method methodRightClick;
    public static void rightClick() {
        try {
            if (methodRightClick == null)
            {
                methodRightClick = Minecraft.class.getDeclaredMethod("func_147121_ag");
                methodRightClick.setAccessible(true);
            }

            methodRightClick.invoke(SigmaClient.mc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void swingItem()
    {
        SigmaClient.mc.thePlayer.swingItem();
    }

    public static void swapToSlot(final int slot) {
        SigmaClient.mc.thePlayer.inventory.currentItem = slot;
    }

    public static void numberClick(final int slot, final int button) {
        SigmaClient.mc.playerController.windowClick(SigmaClient.mc.thePlayer.inventoryContainer.windowId, slot, button, 2, (EntityPlayer) SigmaClient.mc.thePlayer);
    }

    public static void shiftClick(final int slot) {
        SigmaClient.mc.playerController.windowClick(SigmaClient.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, (EntityPlayer) SigmaClient.mc.thePlayer);
    }

    public static void drop(final int slot) {
        SigmaClient.mc.playerController.windowClick(SigmaClient.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, (EntityPlayer) SigmaClient.mc.thePlayer);
    }

    public static int getHotbar(final Predicate<ItemStack> predicate) {
        for (int i = 0; i < 9; ++i) {
            if (SigmaClient.mc.thePlayer.inventory.getStackInSlot(i) != null && predicate.test(SigmaClient.mc.thePlayer.inventory.getStackInSlot(i))) {
                return i;
            }
        }
        return -1;
    }

    public static <T extends Item> int getHotbar(final Class<T> clazz) {
        return getHotbar(stack -> clazz.isAssignableFrom(stack.getItem().getClass()));
    }

    public static int getItem(final String name) {
        final List<Slot> slots = new ArrayList<Slot>(SigmaClient.mc.thePlayer.inventoryContainer.inventorySlots);
        Collections.reverse(slots);
        for (final Slot slot : slots) {
            if (slot.getHasStack() && slot.getStack().getDisplayName().toLowerCase().contains(name.toLowerCase())) {
                return slot.slotNumber;
            }
        }
        return -1;
    }

    public static int getItem(final Predicate<ItemStack> predicate) {
        final List<Slot> slots = new ArrayList<Slot>(SigmaClient.mc.thePlayer.inventoryContainer.inventorySlots);
        Collections.reverse(slots);
        for (final Slot slot : slots) {
            if (slot.getHasStack() && predicate.test(slot.getStack())) {
                return slot.slotNumber;
            }
        }
        return -1;
    }

    public static <T extends Item> int getItem(final Class<T> clazz) {
        final List<Slot> slots = new ArrayList<Slot>(SigmaClient.mc.thePlayer.inventoryContainer.inventorySlots);
        Collections.reverse(slots);
        for (final Slot slot : slots) {
            if (slot.getHasStack() && clazz.isAssignableFrom(slot.getStack().getItem().getClass())) {
                return slot.slotNumber;
            }
        }
        return -1;
    }

    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(SigmaClient.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(SigmaClient.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(SigmaClient.mc.thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(SigmaClient.mc.thePlayer.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(SigmaClient.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(SigmaClient.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = SigmaClient.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        final AxisAlignedBB boundingBox = block.getCollisionBoundingBox((World) SigmaClient.mc.theWorld, new BlockPos(x, y, z), SigmaClient.mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (boundingBox != null && SigmaClient.mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static Vec3 getVectorForRotation(final float yaw, final float pitch) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    public static float getJumpMotion() {
        float motionY = 0.42f;
        if (SigmaClient.mc.thePlayer.isPotionActive(Potion.jump)) {
            motionY += (SigmaClient.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
        }
        return motionY;
    }

    public static float getFriction(final boolean onGround) {
        float f4 = 0.91f;
        if (onGround) {
            f4 = SigmaClient.mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(SigmaClient.mc.thePlayer.posX), MathHelper.floor_double(SigmaClient.mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(SigmaClient.mc.thePlayer.posZ))).getBlock().slipperiness * 0.91f;
        }
        final float f5 = 0.16277136f / (f4 * f4 * f4);
        float f6;
        if (onGround) {
            f6 = SigmaClient.mc.thePlayer.getAIMoveSpeed() * f5;
        }
        else {
            f6 = SigmaClient.mc.thePlayer.jumpMovementFactor;
        }
        return f6;
    }

    public static boolean isOnGround(final double height) {
        return !SigmaClient.mc.theWorld.getCollidingBoundingBoxes((Entity) SigmaClient.mc.thePlayer, SigmaClient.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public static Vec3 getInterpolatedPos(final float partialTicks) {
        return new Vec3(interpolate(SigmaClient.mc.thePlayer.prevPosX, SigmaClient.mc.thePlayer.posX, partialTicks), interpolate(SigmaClient.mc.thePlayer.prevPosY, SigmaClient.mc.thePlayer.posY, partialTicks) + 0.1, interpolate(SigmaClient.mc.thePlayer.prevPosZ, SigmaClient.mc.thePlayer.posZ, partialTicks));
    }

    public static double interpolate(final double prev, final double newPos, final float partialTicks) {
        return prev + (newPos - prev) * partialTicks;
    }

    public static boolean isFall(final float distance) {
        return isFall(distance, 0.0, 0.0);
    }

    public static boolean isFall(final float distance, final double xOffset, final double zOffset) {
        final BlockPos block = new BlockPos(SigmaClient.mc.thePlayer.posX, SigmaClient.mc.thePlayer.posY, SigmaClient.mc.thePlayer.posZ);
        if (!SigmaClient.mc.theWorld.isBlockLoaded(block)) {
            return false;
        }
        final AxisAlignedBB player = SigmaClient.mc.thePlayer.getEntityBoundingBox().offset(xOffset, 0.0, zOffset);
        return SigmaClient.mc.theWorld.getCollidingBoundingBoxes((Entity) SigmaClient.mc.thePlayer, new AxisAlignedBB(player.minX, player.minY - distance, player.minZ, player.maxX, player.maxY, player.maxZ)).isEmpty();
    }

    public static boolean isLiquid(final float distance) {
        return isLiquid(distance, 0.0, 0.0);
    }

    public static boolean isLiquid(final float distance, final double xOffset, final double zOffset) {
        final BlockPos block = new BlockPos(SigmaClient.mc.thePlayer.posX, SigmaClient.mc.thePlayer.posY, SigmaClient.mc.thePlayer.posZ);
        if (!SigmaClient.mc.theWorld.isBlockLoaded(block)) {
            return false;
        }
        final AxisAlignedBB player = SigmaClient.mc.thePlayer.getEntityBoundingBox().offset(xOffset, 0.0, zOffset);
        return SigmaClient.mc.theWorld.isAnyLiquid(new AxisAlignedBB(player.minX, player.minY - distance, player.minZ, player.maxX, player.maxY, player.maxZ));
    }

    public static boolean isOverVoid() {
        return isOverVoid(0.0, 0.0);
    }

    public static boolean isOverVoid(final double xOffset, final double zOffset) {
        final BlockPos block = new BlockPos(SigmaClient.mc.thePlayer.posX, SigmaClient.mc.thePlayer.posY, SigmaClient.mc.thePlayer.posZ);
        if (!SigmaClient.mc.theWorld.isBlockLoaded(block)) {
            return false;
        }
        final AxisAlignedBB player = SigmaClient.mc.thePlayer.getEntityBoundingBox().offset(xOffset, 0.0, zOffset);
        return SigmaClient.mc.theWorld.getCollidingBoundingBoxes(SigmaClient.mc.thePlayer, new AxisAlignedBB(player.minX, 0.0, player.minZ, player.maxX, player.maxY, player.maxZ)).isEmpty();
    }

    public static MovingObjectPosition rayTrace(final float yaw, final float pitch, final float distance) {
        final Vec3 vec3 = SigmaClient.mc.thePlayer.getPositionEyes(1.0f);
        final Vec3 vec4 = getVectorForRotation(yaw, pitch);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * distance, vec4.yCoord * distance, vec4.zCoord * distance);
        return SigmaClient.mc.theWorld.rayTraceBlocks(vec3, vec5, false, true, true);
    }

    public static void syncHeldItem() {
        final int slot = SigmaClient.mc.thePlayer.inventory.currentItem;
        if (slot != ((PlayerControllerAccessor) SigmaClient.mc.playerController).getCurrentPlayerItem()) {
            ((PlayerControllerAccessor) SigmaClient.mc.playerController).setCurrentPlayerItem(slot);
            PacketUtils.sendPacketNoEvent((Packet<?>)new C09PacketHeldItemChange(slot));
        }
    }
}
