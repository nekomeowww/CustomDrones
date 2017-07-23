package com.github.nekomeowww.customdrones.entity.ai;

import java.util.Random;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.entity.EntityDroneMob;

public class DroneAIWander
        extends EntityAIBase
{
    public Random rnd;
    public EntityDroneMob drone;
    public Vec3d wanderPos;
    public double range;
    public double speed;

    public DroneAIWander(EntityDroneMob m, double r, double s)
    {
        this.drone = m;
        this.rnd = new Random();
        this.range = r;
        this.speed = s;
    }

    public boolean func_75250_a()
    {
        return (this.drone.getDroneAttackTarget() == null) && (!this.drone.field_70170_p.field_72995_K);
    }

    public void func_75251_c()
    {
        super.func_75251_c();
        this.wanderPos = null;
    }

    public void func_75246_d()
    {
        super.func_75246_d();
        int baseTickMod = 38 + this.rnd.nextInt(4);
        boolean newPos = (this.wanderPos == null) && (this.drone.field_70173_aa % baseTickMod == 0);
        if ((!newPos) && (this.wanderPos != null)) {
            newPos = ((this.drone.func_70092_e(this.wanderPos.field_72450_a, this.wanderPos.field_72448_b, this.wanderPos.field_72449_c) < 0.2D) && (this.drone.field_70173_aa % (baseTickMod * 2) == 0)) || (this.drone.field_70173_aa % (baseTickMod * 5) == 0);
        }
        if (newPos) {
            this.wanderPos = getWanderPos(this.range);
        }
        if (this.wanderPos != null) {
            this.drone.flyTo(this.wanderPos, 0.4D, this.speed);
        }
    }

    public Vec3d getWanderPos(double range)
    {
        double yWander = this.drone.field_70163_u + (this.rnd.nextDouble() - 0.5D) * range;
        yWander = Math.min(Math.max(yWander, 2.0D), this.drone.getBelowSurfaceY() + range);

        return new Vec3d(this.drone.field_70165_t + (this.rnd.nextDouble() - 0.5D) * range, yWander, this.drone.field_70161_v + (this.rnd.nextDouble() - 0.5D) * range);
    }
}

