package com.github.nekomeowww.customdrones.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.entity.EntityDroneMob;

public class DroneAITargetPlayer
        extends EntityAIBase
{
    public EntityDroneMob drone;
    public double range;

    public DroneAITargetPlayer(EntityDroneMob m, double range)
    {
        this.drone = m;
        this.range = range;
    }

    public boolean shouldExecute()
    {
        return (this.drone.hostile) && (this.drone.getDroneAttackTarget() == null);
    }

    public void updateTask()
    {
        super.updateTask();
        EntityPlayer p = this.drone.world.getNearestPlayerNotCreative(this.drone, this.range);
        if (p != null) {
            this.drone.setDroneAttackTarget(p, true);
        }
    }
}
