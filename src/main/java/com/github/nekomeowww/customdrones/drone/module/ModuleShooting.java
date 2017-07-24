package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.entity.EntityPlasmaShot;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;

public class ModuleShooting
        extends Module
{
    public ModuleShooting(int l, String s)
    {
        super(l, Module.ModuleType.Battle, s);
    }

    public void updateModule(EntityDrone drone)
    {
        super.updateModule(drone);
        double range = getShootingRange(drone);
        EntityPlayer p = drone.getControllingPlayer();
        List<EntityLivingBase> targets;
        if (p != null)
        {
            Entity lae = p.getLastAttacker();
            if ((lae != null) && (lae.isEntityAlive()))
            {
                drone.setDroneAttackTarget(lae, false);
            }
            else
            {
                targets = drone.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, p
                        .getEntityBoundingBox().expand(range, range, range));
                targets.addAll(drone.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, drone
                        .getEntityBoundingBox().expand(range, range, range)));
                for (int a = 0; a < targets.size(); a++)
                {
                    EntityLivingBase e = (EntityLivingBase)targets.get(a);
                    if ((e != null) && (e.isEntityAlive()))
                    {
                        Entity targetee = null;
                        if ((e instanceof EntityLiving)) {
                            targetee = ((EntityLiving)e).getAttackTarget();
                        } else if (e.getLastAttacker() != null) {
                            targetee = e.getLastAttacker();
                        } else if (e.getAITarget() != null) {
                            targetee = e.getAITarget();
                        }
                        if (targetee != null) {
                            if ((targetee == p) || (targetee == drone)) {
                                drone.setDroneAttackTarget(targetee, false);
                            }
                        }
                    }
                }
            }
        }
        if (drone.getDroneAttackTarget() == null)
        {
            List<Entity> targets = drone.getEntityWorld().getEntitiesInAABBexcluding(drone.getRider(), drone
                    .getEntityBoundingBox().expand(range, range, range), IMob.MOB_SELECTOR);
            for (Entity e : targets) {
                drone.setDroneAttackTarget(e, false);
            }
        }
        if (drone.getDroneAttackTarget() != null) {
            if (drone.getDistanceToEntity(drone.getDroneAttackTarget()) > getShootingRange(drone) * 2.0D) {
                drone.setDroneAttackTarget(null, true);
            } else {
                shootAt(drone, drone.getDroneAttackTarget());
            }
        }
    }

    public void shootAt(EntityDrone drone, Entity e)
    {
        double range = getShootingRange(drone);
        double speed = getShootingSpeed(drone);
        double rate = getShootingRate(drone);
        Vec3d mid = EntityHelper.getCenterVec(drone);
        Vec3d eMid = EntityHelper.getCenterVec(e);
        double dist = mid.distanceTo(eMid);
        if (dist <= range) {
            if (drone.attackDelay <= 0)
            {
                drone.attackDelay = ((int)(20.0D / rate));
                if (!drone.getEntityWorld().isRemote)
                {
                    Vec3d startShootVec = VecHelper.fromTo(mid, eMid).normalize();
                    EntityPlasmaShot bullet = new EntityPlasmaShot(drone.getEntityWorld());
                    if (canFunctionAs(shootingHoming))
                    {
                        bullet.homing = true;
                        bullet.homingTarget = e;
                    }
                    bullet.shooter = drone;
                    bullet.damage = drone.droneInfo.getAttackPower(drone);
                    bullet.setPosition(mid.xCoord, mid.yCoord, mid.zCoord);
                    Vec3d dir = VecHelper.setLength(VecHelper.fromTo(bullet.getPositionVector(), eMid), speed);
                    bullet.motionX = dir.xCoord;
                    bullet.motionY = dir.yCoord;
                    bullet.motionZ = dir.zCoord;

                    Color c = new Color(1, 1, 1, 1);
                    switch (drone.droneInfo.core)
                    {
                        case 2:
                            c = new Color(1.0D, 1.0D, 0.5D, 1.0D);
                            break;
                        case 3:
                            c = new Color(0.6D, 1.0D, 0.9D, 1.0D);
                            break;
                        case 4:
                            c = new Color(0.6D, 1.0D, 0.6D, 1.0D);
                    }
                    bullet.color = c;
                    drone.getEntityWorld().spawnEntityInWorld(bullet);
                    drone.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 0.4F, 1.2F + new Random().nextFloat() * 0.8F);
                    drone.droneInfo.reduceBattery(getShootingCost(drone));
                }
            }
        }
    }

    public int overridePriority()
    {
        return 50;
    }

    public void overrideDroneMovement(EntityDrone drone)
    {
        super.overrideDroneMovement(drone);
        int mode = drone.getFlyingMode();
        double speedMult = drone.getSpeedMultiplication();
        Vec3d mid = EntityHelper.getCenterVec(drone);
        Entity e = drone.getDroneAttackTarget();
        Vec3d eMid = EntityHelper.getCenterVec(e);
        double speed = getShootingSpeed(drone);
        double rate = getShootingRate(drone);
        double dist = mid.distanceTo(eMid);
        double range = getShootingRange(drone);
        if (dist > range) {
            if ((mode != 2) && (mode != 3))
            {
                Vec3d followDir = VecHelper.setLength(VecHelper.fromTo(drone.getPositionVector(), eMid), dist - range);
                if (followDir.lengthVector() > 0.6D)
                {
                    if (followDir.lengthVector() > 1.0D) {
                        followDir = followDir.normalize();
                    }
                    drone.motionY += followDir.yCoord * speedMult;
                    drone.motionX += followDir.xCoord * speedMult;
                    drone.motionZ += followDir.zCoord * speedMult;
                }
            }
        }
    }

    public boolean canOverrideDroneMovement(EntityDrone drone)
    {
        int mode = drone.getFlyingMode();

        return (drone.getDroneAttackTarget() != null) && (mode != 2) && (drone.getDistanceSqToEntity(drone.getDroneAttackTarget()) > Math.pow(getShootingRange(drone), 2.0D));
    }

    public double getShootingSpeed(EntityDrone e)
    {
        return 10.0D * e.droneInfo.core / 20.0D;
    }

    public double getShootingRange(EntityDrone e)
    {
        return 8.0D * e.droneInfo.chip;
    }

    public double getShootingRate(EntityDrone e)
    {
        return e.droneInfo.chip / 2.0D;
    }

    public double getShootingCost(EntityDrone e)
    {
        return e.droneInfo.getAttackPower(e) * 4.0D * e.droneInfo.getBatteryConsumptionRate(e);
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleShootingGui(gui, this);
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        list.add("Ranged attack. Target nearby monsters and any entity that attacks the controller.");
        if (canFunctionAs(shootingHoming)) {
            list.add(TextFormatting.YELLOW + "Homing shot.");
        }
    }

    @SideOnly(Side.CLIENT)
    public class ModuleShootingGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        public ModuleShootingGui(T gui)
        {
            super(mod);
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            double damage = this.parent.drone.droneInfo.getAttackPower(this.parent.drone);
            double heart = Math.round(damage / 2.0D * 10.0D) / 10.0D;
            l.add("Attack power: " + TextFormatting.RED + heart + " heart" + (heart > 1.0D ? "s" : ""));
            double speed = ((ModuleShooting)this.mod).getShootingSpeed(this.parent.drone) * 20.0D;
            double range = ((ModuleShooting)this.mod).getShootingRange(this.parent.drone);
            double rate = ((ModuleShooting)this.mod).getShootingRate(this.parent.drone);
            double cost = ((ModuleShooting)this.mod).getShootingCost(this.parent.drone);
            l.add("Speed: " + TextFormatting.GREEN + Math.round(speed * 10.0D) / 10.0D + "m/sec");
            l.add("Range: " + TextFormatting.GREEN + Math.round(range * 10.0D) / 10.0D + "m");
            l.add("Rate: " + TextFormatting.GREEN + Math.round(rate * 10.0D) / 10.0D + " shots/sec");
            l.add("Cost: " + TextFormatting.GREEN + Math.round(cost * 10.0D) / 10.0D + " bat/shot");
        }
    }
}
