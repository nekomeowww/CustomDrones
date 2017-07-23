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
            Entity lae = p.func_110144_aD();
            if ((lae != null) && (lae.func_70089_S()))
            {
                drone.setDroneAttackTarget(lae, false);
            }
            else
            {
                targets = drone.field_70170_p.func_72872_a(EntityLivingBase.class, p
                        .func_174813_aQ().func_72314_b(range, range, range));
                targets.addAll(drone.field_70170_p.func_72872_a(EntityLivingBase.class, drone
                        .func_174813_aQ().func_72314_b(range, range, range)));
                for (int a = 0; a < targets.size(); a++)
                {
                    EntityLivingBase e = (EntityLivingBase)targets.get(a);
                    if ((e != null) && (e.func_70089_S()))
                    {
                        Entity targetee = null;
                        if ((e instanceof EntityLiving)) {
                            targetee = ((EntityLiving)e).func_70638_az();
                        } else if (e.func_110144_aD() != null) {
                            targetee = e.func_110144_aD();
                        } else if (e.func_70643_av() != null) {
                            targetee = e.func_70643_av();
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
            List<Entity> targets = drone.field_70170_p.func_175674_a(drone.getRider(), drone
                    .func_174813_aQ().func_72314_b(range, range, range), IMob.field_82192_a);
            for (Entity e : targets) {
                drone.setDroneAttackTarget(e, false);
            }
        }
        if (drone.getDroneAttackTarget() != null) {
            if (drone.func_70032_d(drone.getDroneAttackTarget()) > getShootingRange(drone) * 2.0D) {
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
        double dist = mid.func_72438_d(eMid);
        if (dist <= range) {
            if (drone.attackDelay <= 0)
            {
                drone.attackDelay = ((int)(20.0D / rate));
                if (!drone.field_70170_p.field_72995_K)
                {
                    Vec3d startShootVec = VecHelper.fromTo(mid, eMid).func_72432_b();
                    EntityPlasmaShot bullet = new EntityPlasmaShot(drone.field_70170_p);
                    if (canFunctionAs(shootingHoming))
                    {
                        bullet.homing = true;
                        bullet.homingTarget = e;
                    }
                    bullet.shooter = drone;
                    bullet.damage = drone.droneInfo.getAttackPower(drone);
                    bullet.func_70107_b(mid.field_72450_a, mid.field_72448_b, mid.field_72449_c);
                    Vec3d dir = VecHelper.setLength(VecHelper.fromTo(bullet.func_174791_d(), eMid), speed);
                    bullet.field_70159_w = dir.field_72450_a;
                    bullet.field_70181_x = dir.field_72448_b;
                    bullet.field_70179_y = dir.field_72449_c;

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
                    drone.field_70170_p.func_72838_d(bullet);
                    drone.func_184185_a(SoundEvents.field_187853_gC, 0.4F, 1.2F + new Random().nextFloat() * 0.8F);
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
        double dist = mid.func_72438_d(eMid);
        double range = getShootingRange(drone);
        if (dist > range) {
            if ((mode != 2) && (mode != 3))
            {
                Vec3d followDir = VecHelper.setLength(VecHelper.fromTo(drone.func_174791_d(), eMid), dist - range);
                if (followDir.func_72433_c() > 0.6D)
                {
                    if (followDir.func_72433_c() > 1.0D) {
                        followDir = followDir.func_72432_b();
                    }
                    drone.field_70181_x += followDir.field_72448_b * speedMult;
                    drone.field_70159_w += followDir.field_72450_a * speedMult;
                    drone.field_70179_y += followDir.field_72449_c * speedMult;
                }
            }
        }
    }

    public boolean canOverrideDroneMovement(EntityDrone drone)
    {
        int mode = drone.getFlyingMode();

        return (drone.getDroneAttackTarget() != null) && (mode != 2) && (drone.func_70068_e(drone.getDroneAttackTarget()) > Math.pow(getShootingRange(drone), 2.0D));
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
