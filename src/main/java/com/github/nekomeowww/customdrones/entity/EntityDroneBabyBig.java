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
        setSize(1.75F, 2.5F);
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
        int count = 4 + i / 2 + this.rand.nextInt(i);
        for (int a = 0; a < count; a++) {
            for (int tryTime = 0; tryTime < 20; tryTime++)
            {
                double x = this.posX + (this.rand.nextDouble() - 0.5D) * 2.0D * range;
                double y = this.posY + (this.rand.nextDouble() - 0.5D) * range;
                double z = this.posZ + (this.rand.nextDouble() - 0.5D) * 2.0D * range;
                Vec3d vec = new Vec3d(x, y, z);
                if (this.getEntityWorld().rayTraceBlocks(EntityHelper.getEyeVec(this), vec, false, true, false) == null)
                {
                    EntityDroneBaby baby = new EntityDroneBaby(this.getEntityWorld());
                    baby.setPosition(x, y, z);
                    baby.hostile = this.hostile;
                    baby.shouldDespawn = this.shouldDespawn;
                    baby.onInitSpawn();

                    DroneInfo di = new DroneInfo(baby, this.rand.nextInt(this.droneInfo.chip) + 1, this.rand.nextInt(this.droneInfo.core) + 1, this.rand.nextInt(this.droneInfo.casing) + 1, this.rand.nextInt(this.droneInfo.engine) + 1);
                    this.getEntityWorld().spawnEntityInWorld(baby);
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
        list.add(new ItemStack(CustomDrones.droneBit, i * 2));

        int i2 = 0;
        for (int a = 0; a < i; a++) {
            if (this.rand.nextInt(4) == 0) {
                i2++;
            }
        }
        if (i2 > 0) {
            list.add(new ItemStack(CustomDrones.droneBit, i2, 1));
        }
        if (this.rand.nextInt(4) == 0) {
            list.add(new ItemStack(getPart("casing", this.droneInfo.casing)));
        }
        if (this.rand.nextInt(12) == 0) {
            list.add(new ItemStack(getPart("chip", this.droneInfo.chip)));
        }
        if (this.rand.nextInt(12) == 0) {
            list.add(new ItemStack(getPart("core", this.droneInfo.core)));
        }
        if (this.rand.nextInt(3) == 0) {
            list.add(new ItemStack(getPart("engine", this.droneInfo.engine)));
        }
    }

    public int getXPOnDeath()
    {
        return addUpParts() * addUpParts();
    }

    public boolean isNonBoss()
    {
        return false;
    }
}
