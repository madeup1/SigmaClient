package git.sigmaclient.utils;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.events.MoveEvent;
import git.sigmaclient.utils.rotation.RotationUtils;
import net.minecraft.entity.Entity;

public class MovementUtils
{
    public static MilliTimer strafeTimer;

    public static float getSpeed() {
        return (float)Math.sqrt(SigmaClient.mc.thePlayer.motionX * SigmaClient.mc.thePlayer.motionX + SigmaClient.mc.thePlayer.motionZ * SigmaClient.mc.thePlayer.motionZ);
    }

    public static float getSpeed(final double x, final double z) {
        return (float)Math.sqrt(x * x + z * z);
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static boolean isMoving() {
        return SigmaClient.mc.thePlayer.moveForward != 0.0f || SigmaClient.mc.thePlayer.moveStrafing != 0.0f;
    }

    public static boolean hasMotion() {
        return SigmaClient.mc.thePlayer.motionX != 0.0 && SigmaClient.mc.thePlayer.motionZ != 0.0 && SigmaClient.mc.thePlayer.motionY != 0.0;
    }

    public static boolean isOnGround(final double height) {
        return !SigmaClient.mc.theWorld.getCollidingBoundingBoxes((Entity) SigmaClient.mc.thePlayer, SigmaClient.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public static void strafe(final double speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        SigmaClient.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        SigmaClient.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
        MovementUtils.strafeTimer.reset();
    }

    public static void bhop(double s) {
        double forward = SigmaClient.mc.thePlayer.movementInput.moveForward;
        double strafe = SigmaClient.mc.thePlayer.movementInput.moveStrafe;
        float yaw = SigmaClient.mc.thePlayer.rotationYaw;

        if ((forward == 0.0D) && (strafe == 0.0D)) {
            SigmaClient.mc.thePlayer.motionX = 0.0D;
            SigmaClient.mc.thePlayer.motionZ = 0.0D;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D)
                    yaw += (float) (forward > 0.0D ? -45 : 45);
                else if (strafe < 0.0D)
                    yaw += (float) (forward > 0.0D ? 45 : -45);

                strafe = 0.0D;
                if (forward > 0.0D)
                    forward = 1.0D;
                else if (forward < 0.0D)
                    forward = -1.0D;
            }

            double rad = Math.toRadians(yaw + 90.0F);
            double sin = Math.sin(rad);
            double cos = Math.cos(rad);
            SigmaClient.mc.thePlayer.motionX = (forward * s * cos) + (strafe * s * sin);
            SigmaClient.mc.thePlayer.motionZ = (forward * s * sin) - (strafe * s * cos);
        }

    }

    public static void strafe(final float speed, final float yaw) {
        if (!isMoving() || !MovementUtils.strafeTimer.hasTimePassed(150L)) {
            return;
        }
        SigmaClient.mc.thePlayer.motionX = -Math.sin(Math.toRadians(yaw)) * speed;
        SigmaClient.mc.thePlayer.motionZ = Math.cos(Math.toRadians(yaw)) * speed;
        MovementUtils.strafeTimer.reset();
    }

    public static void forward(final double length) {
        final double yaw = Math.toRadians(SigmaClient.mc.thePlayer.rotationYaw);
        SigmaClient.mc.thePlayer.setPosition(SigmaClient.mc.thePlayer.posX + -Math.sin(yaw) * length, SigmaClient.mc.thePlayer.posY, SigmaClient.mc.thePlayer.posZ + Math.cos(yaw) * length);
    }

    public static double getDirection() {
        return Math.toRadians(getYaw());
    }

    public static void setMotion(final MoveEvent em, final double speed) {
        double forward = SigmaClient.mc.thePlayer.movementInput.moveForward;
        double strafe = SigmaClient.mc.thePlayer.movementInput.moveStrafe;
        float yaw = SigmaClient.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            SigmaClient.mc.thePlayer.motionX = 0.0;
            SigmaClient.mc.thePlayer.motionZ = 0.0;
            if (em != null) {
                em.setX(0.0);
                em.setZ(0.0);
            }
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            SigmaClient.mc.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
            SigmaClient.mc.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
            if (em != null) {
                em.setX(SigmaClient.mc.thePlayer.motionX);
                em.setZ(SigmaClient.mc.thePlayer.motionZ);
            }
        }
    }

    public static float getYaw() {
        float yaw = SigmaClient.mc.thePlayer.rotationYaw;
        if (SigmaClient.mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (SigmaClient.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (SigmaClient.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (SigmaClient.mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (SigmaClient.mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw;
    }

    static {
        MovementUtils.strafeTimer = new MilliTimer();
    }
}
