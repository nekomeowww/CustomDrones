package com.github.nekomeowww.customdrones.entity.ai;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import williamle.drones.api.Filters.FilterAccepts;
import williamle.drones.entity.EntityDroneMob;

public class DroneAIWanderHerd
        extends DroneAIWander
{
    public Filters.FilterAccepts herdClass;
    public double herdRange = 1.0D;
    public int tryCount = 1;

    public DroneAIWanderHerd(EntityDroneMob m, double r, double s, double herdArea, int herdTry, Class<? extends Entity>... clazz)
    {
        super(m, r, s);
        this.herdClass = new Filters.FilterAccepts((Object[])clazz);
        this.herdRange = herdArea;
        this.tryCount = herdTry;
    }

    public Vec3d getWanderPos(double range)
    {
        double weight = 0.0D;
        Vec3d thisWander = null;
        for (int a = 0; a < this.tryCount; a++)
        {
            double xWander = this.drone.posX + (this.rnd.nextDouble() - 0.5D) * range;
            double zWander = this.drone.posZ + (this.rnd.nextDouble() - 0.5D) * range;
            double yWander = this.drone.posY + (this.rnd.nextDouble() - 0.5D) * range;
            yWander = Math.min(Math.max(yWander, 2.0D), this.drone.getBelowSurfaceY() + 8.0D);
            Vec3d vec = new Vec3d(xWander, yWander, zWander);
            List<Entity> herdFriends = this.drone.world.getEntitiesInAABBexcluding(this.drone, this.drone
                    .getEntityBoundingBox().expandXyz(this.herdRange), this.herdClass);
            double thisWeight = getWeight(herdFriends);
            if ((thisWander == null) || (weight == 0.0D) || (thisWeight > weight))
            {
                thisWander = vec;
                weight = thisWeight;
            }
        }
        return thisWander;
    }

    public double getWeight(List<Entity> herd)
    {
        double d = 0.0D;
        for (Entity e : herd) {
            d += getEntityWeight(e);
        }
        return d;
    }

    public double getEntityWeight(Entity e)
    {
        if ((e instanceof EntityDroneMob)) {
            return ((EntityDroneMob)e).getHerdIndividualWeight();
        }
        return 1.0D;
    }
}
