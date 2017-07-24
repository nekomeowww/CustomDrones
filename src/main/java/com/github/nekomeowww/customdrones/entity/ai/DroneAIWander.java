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

    public boolean shouldExecute()
    {
        return (this.drone.getDroneAttackTarget() == null) && (!this.drone.getEntityWorld().isRemote);
    }

    public void resetTask()
    {
        super.resetTask();
        this.wanderPos = null;
    }

    public void updateTask()
    {
        super.updateTask();
        int baseTickMod = 38 + this.rnd.nextInt(4);
        boolean newPos = (this.wanderPos == null) && (this.drone.ticksExisted % baseTickMod == 0);
        if ((!newPos) && (this.wanderPos != null)) {
            newPos = ((this.drone.getDistanceSq(this.wanderPos.xCoord, this.wanderPos.yCoord, this.wanderPos.zCoord) < 0.2D) && (this.drone.ticksExisted % (baseTickMod * 2) == 0)) || (this.drone.ticksExisted % (baseTickMod * 5) == 0);
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
        double yWander = this.drone.posY + (this.rnd.nextDouble() - 0.5D) * range;
        yWander = Math.min(Math.max(yWander, 2.0D), this.drone.getBelowSurfaceY() + range);

        return new Vec3d(this.drone.posX + (this.rnd.nextDouble() - 0.5D) * range, yWander, this.drone.posZ + (this.rnd.nextDouble() - 0.5D) * range);
    }
}

