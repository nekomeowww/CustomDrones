package com.github.nekomeowww.customdrones.entity.ai;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.entity.EntityDroneMob;

public class DroneAIFlyToNearest
        extends EntityAIBase
{
    public Random rnd;
    public EntityDroneMob drone;
    public double range;
    public double speed;
    public Class<? extends Entity> clazz;

    public DroneAIFlyToNearest(EntityDroneMob m, double r, double s, Class<? extends Entity> clazz)
    {
        this.drone = m;
        this.rnd = new Random();
        this.range = r;
        this.speed = s;
        this.clazz = clazz;
    }

    public boolean shouldExecute()
    {
        return this.drone.world.findNearestEntityWithinAABB(this.clazz, this.drone.getEntityBoundingBox().expandXyz(this.range), this.drone) != null;
    }

    public void updateTask()
    {
        super.updateTask();
        Entity e = this.drone.world.findNearestEntityWithinAABB(this.clazz, this.drone.getEntityBoundingBox().expandXyz(this.range), this.drone);

        this.drone.flyTo(EntityHelper.getEyeVec(e), 0.2D, this.speed);
    }
}

