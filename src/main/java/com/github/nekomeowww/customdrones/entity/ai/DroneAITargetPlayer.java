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

    public boolean func_75250_a()
    {
        return (this.drone.hostile) && (this.drone.getDroneAttackTarget() == null);
    }

    public void func_75246_d()
    {
        super.func_75246_d();
        EntityPlayer p = this.drone.field_70170_p.func_184136_b(this.drone, this.range);
        if (p != null) {
            this.drone.setDroneAttackTarget(p, true);
        }
    }
}
