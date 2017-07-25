package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.api.Filters;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.api.helpers.WorldHelper;
import com.github.nekomeowww.customdrones.api.path.Node;
import com.github.nekomeowww.customdrones.api.path.Path;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;
import com.github.nekomeowww.customdrones.item.ItemDroneFlyer;

public class ModuleMovement
        extends Module
{
    public ModuleMovement(int l, String s)
    {
        super(l, Module.ModuleType.Scout, s);
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleMovementGui(gui, this);
    }

    public void overrideDroneMovement(EntityDrone drone)
    {
        int mode = drone.getFlyingMode();
        if ((mode == 2) || ((mode == 3) && (drone.recordingPath != null)))
        {
            EntityPlayer p = drone.getEntityWorld().getClosestPlayerToEntity(drone, 64 * drone.droneInfo.chip);
            if ((p != null) && (p.getHeldItemMainhand() != null) &&
                    (CustomDrones.droneFlyer.isControllingDrone(p.getHeldItemMainhand(), drone)))
            {
                drone.idle = false;
                int flyMode = CustomDrones.droneFlyer.getFlyMode(p.getHeldItemMainhand());
                if (flyMode == 1)
                {
                    Vec3d maxTarget = EntityHelper.getEyeVec(p).add(VecHelper.setLength(p.getLookVec(), 32.0D));
                    RayTraceResult mop = WorldHelper.fullRayTrace(drone.getEntityWorld(), EntityHelper.getEyeVec(p), maxTarget,
                            !p.isInWater(), false, new Filters.FilterExcepts(new Object[] { drone, p, drone.getRider(), EntityXPOrb.class }));
                    Vec3d dest = maxTarget;
                    if (mop != null) {
                        if (mop.entityHit != null) {
                            dest = EntityHelper.getEyeVec(mop.entityHit);
                        } else if (mop.hitVec != null) {
                            dest = mop.hitVec;
                        }
                    }
                    dest = dest.addVector(0.0D, drone.getRiderHeight(), 0.0D);
                    Vec3d dirNorm = VecHelper.fromTo(EntityHelper.getEyeVec(drone), dest);
                    drone.flyNormalAlong(dirNorm, 0.6D, 1.0D);
                }
                else if (flyMode == 2)
                {
                    drone.flyNormalAlong(p.getLookVec(), 0.0D, 1.0D);
                }
                if (drone.recordingPath != null) {
                    if ((drone.prevMotionX != drone.motionX) || (drone.prevMotionY != drone.motionY) || (drone.prevMotionZ != drone.motionZ))
                    {
                        Node n = new Node(drone.posX, drone.posY, drone.posZ);
                        drone.recordingPath.addNode(n);
                    }
                }
            }
            else
            {
                drone.idle = true;
            }
        }
        else if (mode == 3)
        {
            drone.idle = false;
            if ((drone.automatedPath != null) && (drone.automatedPath.hasPath()))
            {
                Node nowNode = drone.automatedPath.getCurrentNode();
                Vec3d nowNodeVec = nowNode.toVec();
                Vec3d pos = drone.getPositionVector();
                Vec3d flyVec = null;
                flyVec = VecHelper.fromTo(pos, nowNodeVec);
                if (flyVec != null)
                {
                    double speedMult = drone.getSpeedMultiplication();
                    double minDist = speedMult * 20.0D;
                    flyVec = flyVec.normalize();
                    drone.motionX += flyVec.xCoord * speedMult;
                    drone.motionY += flyVec.yCoord * speedMult;
                    drone.motionZ += flyVec.zCoord * speedMult;
                    if (pos.addVector(drone.motionX, drone.motionY, drone.motionZ).squareDistanceTo(nowNodeVec) < minDist * minDist) {
                        if (drone.automatedPath.goToNextNode() == null) {
                            drone.automatedPath.resetPathIndex();
                        }
                    }
                }
            }
        }
        else if (mode == 4)
        {
            drone.idle = false;
            EntityPlayer toFollow = drone.getControllingPlayer();
            if (toFollow != null)
            {
                Vec3d vecTo = new Vec3d(toFollow.posX, toFollow.posY + toFollow.getEyeHeight(), toFollow.posZ);
                Vec3d vecFrom = drone.getPositionVector();
                double dist = vecFrom.distanceTo(vecTo);
                double minDist = Math.pow(drone.droneInfo.engine, 0.35D) * 2.5D;
                if (dist > minDist)
                {
                    Vec3d followDir = VecHelper.setLength(VecHelper.fromTo(vecFrom, vecTo), dist - minDist);
                    drone.flyNormalAlong(followDir, 0.6D, 1.0D);
                }
            }
            else
            {
                drone.idle = true;
            }
        }
    }

    public int overridePriority()
    {
        return 0;
    }

    public boolean canOverrideDroneMovement(EntityDrone drone)
    {
        return drone.getFlyingMode() > 1;
    }

    public void onDisabled(EntityDrone drone)
    {
        super.onDisabled(drone);
        if (!drone.canDroneHaveFlyMode(drone.getFlyingMode())) {
            drone.setNextFlyingMode();
        }
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        if (this == controlMovement) {
            list.add("Allow manual control movement");
        } else if (this == pathMovement) {
            list.add("Allow preset path movement");
        } else if (this == followMovement) {
            list.add("Allow following movement");
        } else if (this == multiMovement) {
            list.add("Allow manual control, preset path, and following movement");
        }
    }

    @SideOnly(Side.CLIENT)
    public class ModuleMovementGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        public ModuleMovementGui(GuiDroneStatus gui, T mod)
        {
            super(gui, mod);
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            if (this.parent.drone.getControllingPlayer() != null) {
                l.add("Controlled by " + TextFormatting.GREEN + this.parent.drone.getControllingPlayer().getName());
            }
        }
    }
}
