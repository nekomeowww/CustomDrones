package com.github.nekomeowww.customdrones.entity.ai;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.entity.EntityDroneMob;

public class DroneAIAttackMelee
        extends EntityAIBase
{
    public Random rnd;
    public EntityDroneMob drone;

    public DroneAIAttackMelee(EntityDroneMob m)
    {
        this.drone = m;
        this.rnd = new Random();
    }

    public boolean shouldExecute()
    {
        return this.drone.getDroneAttackTarget() != null;
    }

    public void startExecuting()
    {
        super.startExecuting();
        this.drone.droneInfo.switchModule(this.drone, this.drone.droneInfo.getModuleWithFunctionOf(Module.weapon1), true);
    }

    public void resetTask()
    {
        super.resetTask();
        this.drone.droneInfo.switchModule(this.drone, this.drone.droneInfo.getModuleWithFunctionOf(Module.weapon1), false);
    }

    public void updateTask()
    {
        super.updateTask();
        Entity target = this.drone.getDroneAttackTarget();
        this.drone.flyTo(EntityHelper.getCenterVec(target), 0.0D, 1.0D);
        if (((target instanceof EntityPlayer)) && (((EntityPlayer)target).capabilities.disableDamage)) {
            this.drone.setDroneAttackTarget(null, true);
        }
    }
}
