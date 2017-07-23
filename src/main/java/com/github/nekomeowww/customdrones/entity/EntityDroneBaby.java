package com.github.nekomeowww.customdrones.entity;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.entity.ai.DroneAIAttackMelee;
import com.github.nekomeowww.customdrones.entity.ai.DroneAITargetPlayer;
import com.github.nekomeowww.customdrones.entity.ai.DroneAIWanderHerd;
import com.github.nekomeowww.customdrones.render.DroneModels;
import com.github.nekomeowww.customdrones.render.DroneModels.ModelProp;

public class EntityDroneBaby
        extends EntityDroneMob
{
    public EntityDroneBaby(World worldIn)
    {
        super(worldIn);
        func_70105_a(0.3F, 0.4F);
        this.modelScale = 0.5D;
    }

    public void initDroneAI()
    {
        this.droneTasks.func_75776_a(7, new DroneAITargetPlayer(this, 12.0D));
        this.droneTasks.func_75776_a(8, new DroneAIAttackMelee(this));
        this.droneTasks.func_75776_a(10, new DroneAIWanderHerd(this, 16.0D, 0.5D, 16.0D, 5, new Class[] { getClass() }));
    }

    public void initDroneAIPostSpawn() {}

    public void initDroneInfo()
    {
        if ((this.droneInfo.casing == 1) && (this.droneInfo.chip == 1) && (this.droneInfo.core == 1) && (this.droneInfo.engine == 1)) {
            randomizeDroneParts();
        }
        super.initDroneInfo();
    }

    public void initSpawnSetAppearance()
    {
        this.droneInfo.appearance.modelID = DroneModels.instance.baby.id;
        super.initSpawnSetAppearance();
    }

    public void initSpawnAddModules()
    {
        this.droneInfo.mods.clear();
        this.droneInfo.applyModule(Module.getModule("Battery Saving " + DroneInfo.greekNumber[this.droneInfo.chip]));
        this.droneInfo.applyModule(Module.getModule("Weapon " + DroneInfo.greekNumber[this.droneInfo.core]));
        this.droneInfo.applyModule(Module.getModule("Armor " + DroneInfo.greekNumber[this.droneInfo.casing]));
        this.droneInfo.applyModule(Module.multiMovement);
        this.droneInfo.switchModule(this, this.droneInfo.getModuleWithFunctionOf(Module.weapon1), false);
    }

    public boolean func_70097_a(DamageSource source, float amount)
    {
        if (source.func_76346_g() != null)
        {
            if ((getDroneAttackTarget() == null) || ((!(getDroneAttackTarget() instanceof EntityPlayer)) &&
                    ((source.func_76346_g() instanceof EntityPlayer)))) {
                setDroneAttackTarget(source.func_76346_g(), true);
            } else {
                setDroneAttackTarget(source.func_76346_g(), false);
            }
            callNearbyBabiesToAttack(source.func_76346_g(), 16.0D);
        }
        return super.func_70097_a(source, amount);
    }

    public void callNearbyBabiesToAttack(Entity e, double range)
    {
        List<EntityDroneBaby> mobs = this.field_70170_p.func_72872_a(EntityDroneBaby.class,
                func_174813_aQ().func_186662_g(range));
        mobs.remove(this);
        for (EntityDroneBaby mob : mobs) {
            if ((mob.getDroneAttackTarget() == null) || (
                    (!(mob.getDroneAttackTarget() instanceof EntityPlayer)) && ((e instanceof EntityPlayer)))) {
                mob.setDroneAttackTarget(e, true);
            }
        }
    }

    public void addDropsOnDeath(List<ItemStack> list)
    {
        int chanceCount = addUpParts();
        int i = this.field_70146_Z.nextInt(chanceCount * 2);
        if (i > 0) {
            list.add(new ItemStack(DronesMod.droneBit, i));
        }
        if (this.field_70146_Z.nextInt(Math.max(100 - chanceCount, 1)) == 0) {
            list.add(new ItemStack(DronesMod.droneBit, 1, 1));
        }
        if (this.field_70146_Z.nextInt(20) == 0) {
            list.add(new ItemStack(getPart("casing", this.droneInfo.casing)));
        }
        if (this.field_70146_Z.nextInt(60) == 0) {
            list.add(new ItemStack(getPart("chip", this.droneInfo.chip)));
        }
        if (this.field_70146_Z.nextInt(60) == 0) {
            list.add(new ItemStack(getPart("core", this.droneInfo.core)));
        }
        if (this.field_70146_Z.nextInt(15) == 0) {
            list.add(new ItemStack(getPart("engine", this.droneInfo.engine)));
        }
    }

    public int getXPOnDeath()
    {
        return addUpParts();
    }

    public boolean shouldAttackDrone(EntityDrone e)
    {
        return (super.shouldAttackDrone(e)) && (!(e instanceof EntityDroneBaby));
    }

    public void func_70037_a(NBTTagCompound tagCompound)
    {
        super.func_70037_a(tagCompound);
        this.droneInfo.appearance.modelID = DroneModels.instance.baby.id;
    }
}
