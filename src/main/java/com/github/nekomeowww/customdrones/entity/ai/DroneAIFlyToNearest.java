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

    public boolean func_75250_a()
    {
        return this.drone.field_70170_p.func_72857_a(this.clazz, this.drone.func_174813_aQ().func_186662_g(this.range), this.drone) != null;
    }

    public void func_75246_d()
    {
        super.func_75246_d();
        Entity e = this.drone.field_70170_p.func_72857_a(this.clazz, this.drone.func_174813_aQ().func_186662_g(this.range), this.drone);

        this.drone.flyTo(EntityHelper.getEyeVec(e), 0.2D, this.speed);
    }
}

