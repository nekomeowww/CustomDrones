package com.github.nekomeowww.customdrones.entity;

import java.util.List;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.drone.DroneInfo;

public class EntityDroneBabyBig
        extends EntityDroneBaby
{
    public double minionSpawnRange = 16.0D;

    public EntityDroneBabyBig(World worldIn)
    {
        super(worldIn);
        func_70105_a(1.75F, 2.5F);
        this.modelScale = 3.0D;
    }

    public void onInitSpawn()
    {
        super.onInitSpawn();
        spawnMinions(this.minionSpawnRange);
    }

    public void spawnMinions(double range)
    {
        if (range <= 0.0D) {
            return;
        }
        int i = addUpParts();
        int count = 4 + i / 2 + this.field_70146_Z.nextInt(i);
        for (int a = 0; a < count; a++) {
            for (int tryTime = 0; tryTime < 20; tryTime++)
            {
                double x = this.field_70165_t + (this.field_70146_Z.nextDouble() - 0.5D) * 2.0D * range;
                double y = this.field_70163_u + (this.field_70146_Z.nextDouble() - 0.5D) * range;
                double z = this.field_70161_v + (this.field_70146_Z.nextDouble() - 0.5D) * 2.0D * range;
                Vec3d vec = new Vec3d(x, y, z);
                if (this.field_70170_p.func_147447_a(EntityHelper.getEyeVec(this), vec, false, true, false) == null)
                {
                    EntityDroneBaby baby = new EntityDroneBaby(this.field_70170_p);
                    baby.func_70107_b(x, y, z);
                    baby.hostile = this.hostile;
                    baby.shouldDespawn = this.shouldDespawn;
                    baby.onInitSpawn();

                    DroneInfo di = new DroneInfo(baby, this.field_70146_Z.nextInt(this.droneInfo.chip) + 1, this.field_70146_Z.nextInt(this.droneInfo.core) + 1, this.field_70146_Z.nextInt(this.droneInfo.casing) + 1, this.field_70146_Z.nextInt(this.droneInfo.engine) + 1);
                    this.field_70170_p.func_72838_d(baby);
                    break;
                }
            }
        }
    }

    public double getBaseAttack()
    {
        return 5.0D;
    }

    public double getBaseHealth()
    {
        return 100.0D;
    }

    public int getHerdIndividualWeight()
    {
        return 30;
    }

    public double getSpeedMultiplication()
    {
        return Math.sqrt(super.getSpeedMultiplication()) * 0.5D;
    }

    public void addDropsOnDeath(List<ItemStack> list)
    {
        super.addDropsOnDeath(list);
        int i = addUpParts();
        list.add(new ItemStack(DronesMod.droneBit, i * 2));

        int i2 = 0;
        for (int a = 0; a < i; a++) {
            if (this.field_70146_Z.nextInt(4) == 0) {
                i2++;
            }
        }
        if (i2 > 0) {
            list.add(new ItemStack(DronesMod.droneBit, i2, 1));
        }
        if (this.field_70146_Z.nextInt(4) == 0) {
            list.add(new ItemStack(getPart("casing", this.droneInfo.casing)));
        }
        if (this.field_70146_Z.nextInt(12) == 0) {
            list.add(new ItemStack(getPart("chip", this.droneInfo.chip)));
        }
        if (this.field_70146_Z.nextInt(12) == 0) {
            list.add(new ItemStack(getPart("core", this.droneInfo.core)));
        }
        if (this.field_70146_Z.nextInt(3) == 0) {
            list.add(new ItemStack(getPart("engine", this.droneInfo.engine)));
        }
    }

    public int getXPOnDeath()
    {
        return addUpParts() * addUpParts();
    }

    public boolean func_184222_aU()
    {
        return false;
    }
}
