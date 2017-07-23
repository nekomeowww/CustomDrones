package com.github.nekomeowww.customdrones.drone.module;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;

public class ModuleDe
        extends Module
{
    public ModuleDe(int l, String s)
    {
        super(l, Module.ModuleType.Battle, s);
    }

    public void updateModule(EntityDrone drone)
    {
        boolean projectile = canFunctionAs(deflect);
        boolean fire = canFunctionAs(deflame);
        boolean explosion = canFunctionAs(deexplosion);
        super.updateModule(drone);
        int count = 0;
        double range = getRange(drone);
        Vec3d deflectMid;
        double strength;
        if (projectile)
        {
            List<Entity> projectiles = drone.field_70170_p.func_175647_a(Entity.class, drone
                    .func_174813_aQ().func_186662_g(range), new Predicate()
            {
                public boolean apply(Entity input)
                {
                    return input instanceof IProjectile;
                }
            });
            deflectMid = EntityHelper.getCenterVec(drone.getControllingPlayer() != null ? drone.getControllingPlayer() : drone);
            strength = getStrength(drone);
            for (Entity e : projectiles)
            {
                double dist = Math.pow(e.func_70032_d(drone), 1.4D);
                Vec3d deflectVec = EntityHelper.getCenterVec(e).func_178788_d(deflectMid).func_72432_b();
                deflectVec = VecHelper.scale(deflectVec, strength / dist);
                e.func_70024_g(deflectVec.field_72450_a, deflectVec.field_72448_b, deflectVec.field_72449_c);
                count++;
            }
        }
        if ((fire) && (drone.field_70173_aa % 20 == 0))
        {
            int maxFire = getFirePerSec(drone);
            int fireCount = 0;
            EntityPlayer player = drone.getControllingPlayer() != null ? drone.getControllingPlayer() : null;
            for (int i = 0; i < range; i++)
            {
                int xmin = (int)Math.floor(drone.field_70165_t - i);
                int xmax = (int)Math.floor(drone.field_70165_t + i);
                int ymin = (int)Math.floor(drone.field_70163_u - i);
                int ymax = (int)Math.floor(drone.field_70163_u + i);
                int zmin = (int)Math.floor(drone.field_70161_v - i);
                int zmax = (int)Math.floor(drone.field_70161_v + i);
                for (int x = xmin; x <= xmax; x++)
                {
                    if (fireCount == maxFire) {
                        break;
                    }
                    for (int y = ymin; y <= ymax; y++)
                    {
                        if (fireCount == maxFire) {
                            break;
                        }
                        for (int z = zmin; z <= zmax; z++)
                        {
                            if (fireCount == maxFire) {
                                break;
                            }
                            if ((x == xmin) || (x == xmax) || (z == zmin) || (z == zmax) || (y == ymin) || (y == ymax))
                            {
                                BlockPos bp = new BlockPos(x, y, z);
                                IBlockState bs = drone.field_70170_p.func_180495_p(bp);
                                Block b = bs.func_177230_c();
                                if (b == Blocks.field_150480_ab)
                                {
                                    fireCount++;
                                    count++;
                                    drone.field_70170_p.func_180498_a(player, 1009, bp, 0);
                                    drone.field_70170_p.func_175698_g(bp);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (explosion) {}
        drone.droneInfo.reduceBattery(count * 5 * drone.droneInfo.getBatteryConsumptionRate(drone));
    }

    public double getRange(EntityDrone drone)
    {
        return drone.droneInfo.chip * 2;
    }

    public static double getMaxPossibleRange()
    {
        return 8.0D;
    }

    public double getStrength(EntityDrone drone)
    {
        return drone.droneInfo.core * 0.5D;
    }

    public int getFirePerSec(EntityDrone drone)
    {
        return drone.droneInfo.core * 4;
    }

    public double getMaxExplosionSize(EntityDrone drone)
    {
        return drone.droneInfo.core * 4;
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleDeGui(gui, this);
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        boolean projectile = canFunctionAs(deflect);
        boolean fire = canFunctionAs(deflame);
        boolean explosion = canFunctionAs(deexplosion);
        if (projectile) {
            list.add("Deflect incoming projectiles");
        }
        if (fire) {
            list.add("Extinguish nearby fire");
        }
        if (explosion) {
            list.add("Negate nearby explosions");
        }
    }

    @SideOnly(Side.CLIENT)
    public class ModuleDeGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        boolean projectile;
        boolean fire;
        boolean explosion;

        public ModuleDeGui(T gui)
        {
            super(mod);
            this.projectile = mod.canFunctionAs(Module.deflect);
            this.fire = mod.canFunctionAs(Module.deflame);
            this.explosion = mod.canFunctionAs(Module.deexplosion);
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            int diameter = (int)(ModuleDe.this.getRange(this.parent.drone) * 2.0D);
            l.add("Effective range: " + TextFormatting.YELLOW + diameter + "x" + diameter + "x" + diameter + TextFormatting.RESET + " blocks");
            if (this.projectile) {
                l.add("Deflecting power multiplier: " + TextFormatting.RED + "x" + this.parent.drone.droneInfo.core);
            }
            if (this.fire) {
                l.add("Fire extinguish per second: " + TextFormatting.RED + ModuleDe.this.getFirePerSec(this.parent.drone));
            }
        }
    }
}
