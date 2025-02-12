package git.sigmaclient.utils.rotation;

import java.util.*;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.mixins.player.PlayerSPAccessor;
import git.sigmaclient.utils.MovementUtils;
import net.minecraft.util.*;
import net.minecraft.entity.*;

public class RotationUtils
{
    public static float lastLastReportedPitch;

    private RotationUtils() {
    }

    public static Rotation getClosestRotation(final AxisAlignedBB aabb) {
        return getRotations(getClosestPointInAABB(SigmaClient.mc.thePlayer.getPositionEyes(1.0f), aabb));
    }

    public static Rotation getClosestRotation(final AxisAlignedBB aabb, final float offset) {
        return getClosestRotation(aabb.expand((double)(-offset), (double)(-offset), (double)(-offset)));
    }

    public static Rotation getRotations(final EntityLivingBase target) {
        return getRotations(target.posX, target.posY + target.getEyeHeight() / 2.0, target.posZ);
    }

    public static Rotation getRotations(final EntityLivingBase target, final float random) {
        return getRotations(target.posX + (new Random().nextInt(3) - 1) * random * new Random().nextFloat(), target.posY + target.getEyeHeight() / 2.0 + (new Random().nextInt(3) - 1) * random * new Random().nextFloat(), target.posZ + (new Random().nextInt(3) - 1) * random * new Random().nextFloat());
    }

    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), getAngleDifference(a.getPitch(), b.getPitch()));
    }

    public static Rotation getRotations(final Vec3 vec3) {
        return getRotations(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public static Rotation getScaffoldRotations(final BlockPos position) {
        final double direction = MovementUtils.getDirection();
        final double posX = -Math.sin(direction) * 0.5;
        final double posZ = Math.cos(direction) * 0.5;
        final double x = position.getX() - SigmaClient.mc.thePlayer.posX - posX;
        final double y = position.getY() - SigmaClient.mc.thePlayer.prevPosY - SigmaClient.mc.thePlayer.getEyeHeight();
        final double z = position.getZ() - SigmaClient.mc.thePlayer.posZ - posZ;
        final double distance = Math.hypot(x, z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793 - 90.0);
        final float pitch = (float)(-(Math.atan2(y, distance) * 180.0 / 3.141592653589793));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getRotations(final double posX, final double posY, final double posZ) {
        final double x = posX - SigmaClient.mc.thePlayer.posX;
        final double y = posY - (SigmaClient.mc.thePlayer.posY + SigmaClient.mc.thePlayer.getEyeHeight());
        final double z = posZ - SigmaClient.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getSmoothRotation(final Rotation current, final Rotation target, final float smooth) {
        return new Rotation(current.getYaw() + (target.getYaw() - current.getYaw()) / smooth, current.getPitch() + (target.getPitch() - current.getPitch()) / smooth);
    }

    public static Rotation getLastReportedRotation() {
        return new Rotation(((PlayerSPAccessor) SigmaClient.mc.thePlayer).getLastReportedYaw(), ((PlayerSPAccessor) SigmaClient.mc.thePlayer).getLastReportedPitch());
    }

    public static Rotation getPlayerRotation() {
        return new Rotation(SigmaClient.mc.thePlayer.rotationYaw, SigmaClient.mc.thePlayer.rotationPitch);
    }

    public static Rotation getLimitedRotation(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        return new Rotation(currentRotation.getYaw() + MathHelper.clamp_float(getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw()), -turnSpeed, turnSpeed), currentRotation.getPitch() + MathHelper.clamp_float(getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch()), -turnSpeed, turnSpeed));
    }

    public static float getAngleDifference(final float a, final float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static Rotation getBowRotation(final Entity entity) {
        final double xDelta = (entity.posX - entity.lastTickPosX) * 0.4;
        final double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4;
        double d = SigmaClient.mc.thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        final double xMulti = d / 0.8 * xDelta;
        final double zMulti = d / 0.8 * zDelta;
        final double x = entity.posX + xMulti - SigmaClient.mc.thePlayer.posX;
        final double z = entity.posZ + zMulti - SigmaClient.mc.thePlayer.posZ;
        final double y = SigmaClient.mc.thePlayer.posY + SigmaClient.mc.thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = SigmaClient.mc.thePlayer.getDistanceToEntity(entity);
        final float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        final float pitch = (float)(-(Math.atan2(y, d2) * 180.0 / 3.141592653589793)) + (float)dist * 0.11f;
        return new Rotation(yaw, -pitch);
    }

    public static Vec3 getClosestPointInAABB(final Vec3 vec3, final AxisAlignedBB aabb) {
        return new Vec3(clamp(aabb.minX, aabb.maxX, vec3.xCoord), clamp(aabb.minY, aabb.maxY, vec3.yCoord), clamp(aabb.minZ, aabb.maxZ, vec3.zCoord));
    }

    private static double clamp(final double min, final double max, final double value) {
        return Math.max(min, Math.min(max, value));
    }
}