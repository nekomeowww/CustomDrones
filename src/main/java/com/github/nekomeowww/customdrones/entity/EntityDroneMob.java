package com.github.nekomeowww.customdrones.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeBeach;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.biome.BiomeHell;
import net.minecraft.world.biome.BiomeHills;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.biome.BiomeRiver;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraft.world.biome.BiomeSwamp;
import net.minecraft.world.biome.BiomeTaiga;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import com.github.nekomeowww.customdrones.drone.DroneAppearance.ColorPalette;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.item.ItemDronePart;

public abstract class EntityDroneMob
        extends EntityDrone
        implements IMob
{
    public int MOBID = 55537;
    public EntityAITasks droneTasks;
    public double modelScale;
    public Entity lastAttacker;
    public boolean hostile;
    public boolean shouldDespawn;

    public EntityDroneMob(World worldIn)
    {
        super(worldIn);
        this.modelScale = 1.0D;
        //1.6.1-beta server error
        //NullPointerException
        /*
        this.droneTasks = new EntityAITasks((worldIn != null) && (worldIn.theProfiler != null) ? worldIn.theProfiler : null);
        */
        if((worldIn != null) && (worldIn.theProfiler != null))
        {
            this.droneTasks = new EntityAITasks(worldIn.theProfiler);
        }
        if ((worldIn != null) && (!worldIn.isRemote)) {
            initDroneAI();
        }
    }

    protected boolean canDespawn()
    {
        return this.shouldDespawn;
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        onInitSpawn();
        return super.onInitialSpawn(difficulty, livingdata);
    }

    public abstract void initDroneAI();

    public abstract void initDroneAIPostSpawn();

    public void onInitSpawn()
    {
        initDroneInfo();
        initDroneAIPostSpawn();
    }

    public void initDroneInfo()
    {
        this.droneInfo.droneFreq = this.MOBID;
        initSpawnSetAppearance();
        initSpawnAddModules();
    }

    public void initSpawnSetAppearance()
    {
        setAppearanceBasedOnBiome();
    }

    public abstract void initSpawnAddModules();

    public void randomizeDroneParts()
    {
        double log2 = Math.log(2.0D);
        this.droneInfo.casing = (4 - (int)Math.floor(Math.log(this.rand.nextInt(15) + 1) / log2));
        this.droneInfo.chip = (4 - (int)Math.floor(Math.log(this.rand.nextInt(15) + 1) / log2));
        this.droneInfo.core = (4 - (int)Math.floor(Math.log(this.rand.nextInt(15) + 1) / log2));
        this.droneInfo.engine = (4 - (int)Math.floor(Math.log(this.rand.nextInt(15) + 1) / log2));
    }

    public void setAppearanceBasedOnBiome()
    {
        Biome biome = this.getEntityWorld().getBiomeForCoordsBody(getPosition());
        if (((biome instanceof BiomeSnow)) || (this.getEntityWorld().canSnowAtBody(getPosition(), false))) {
            this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Ice glacier"));
        } else if (((biome instanceof BiomeHell)) || ((biome instanceof BiomeMesa))) {
            this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Sunset"));
        } else if (((biome instanceof BiomeBeach)) || ((biome instanceof BiomeDesert))) {
            this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Sandy desert"));
        } else if (((biome instanceof BiomeRiver)) || ((biome instanceof BiomeOcean))) {
            this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Sea waves"));
        } else if (((biome instanceof BiomeForest)) || ((biome instanceof BiomeHills)) || ((biome instanceof BiomeJungle)) || ((biome instanceof BiomePlains)) || ((biome instanceof BiomeTaiga)) || ((biome instanceof BiomeSwamp)) || ((biome instanceof BiomeSavanna))) {
            this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Grass roots"));
        } else {
            setDefaultAppearance();
        }
        Color c = new Color(0.8D, 0.8D, 0.8D, 1.0D);
        switch (this.droneInfo.core)
        {
            case 2:
                c = new Color(1.0D, 1.0D, 0.5D, 1.0D);
                break;
            case 3:
                c = new Color(0.6D, 1.0D, 0.9D, 1.0D);
                break;
            case 4:
                c = new Color(0.6D, 1.0D, 0.6D, 1.0D);
        }
        this.droneInfo.appearance.palette.setPaletteColor("Core", c.copy());
        c = new Color(0.8D, 0.8D, 0.8D, 1.0D);
        switch (this.droneInfo.engine)
        {
            case 2:
                c = new Color(1.0D, 1.0D, 0.5D, 1.0D);
                break;
            case 3:
                c = new Color(0.6D, 1.0D, 0.9D, 1.0D);
                break;
            case 4:
                c = new Color(0.6D, 1.0D, 0.6D, 1.0D);
        }
        this.droneInfo.appearance.palette.setPaletteColor("Wing", c.copy());
    }

    public void setDefaultAppearance()
    {
        this.droneInfo.appearance.palette = DroneAppearance.ColorPalette.fastMake("Mob", Integer.valueOf(-10066330));
    }

    public int getHerdIndividualWeight()
    {
        return 1;
    }

    public boolean usefulInteraction(EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand, boolean hasController)
    {
        return false;
    }

    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        this.droneTasks.onUpdateTasks();
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (source != null) {
            this.lastAttacker = source.getEntity();
        }
        return super.attackEntityFrom(source, amount);
    }

    public float getEyeHeight()
    {
        return this.height / 2.0F;
    }

    public int getFlyingMode()
    {
        return this.MOBID;
    }

    public void setDead()
    {
        super.setDead();
        if ((!this.getEntityWorld().isRemote) && (this.getEntityWorld().getGameRules().getBoolean("doMobLoot")))
        {
            List<ItemStack> itemsToDrop = new ArrayList();
            addDropsOnDeath(itemsToDrop);
            for (ItemStack is : itemsToDrop) {
                entityDropItem(is, this.rand.nextFloat() * this.height);
            }
            if (((this.lastAttacker instanceof EntityPlayer)) || (((this.lastAttacker instanceof EntityDrone)) &&
                    (((EntityDrone)this.lastAttacker).getControllingPlayer() != null)))
            {
                int i = getXPOnDeath();
                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.getEntityWorld().spawnEntityInWorld(new EntityXPOrb(this.getEntityWorld(), this.posX, this.posY, this.posZ, j));
                }
            }
        }
    }

    public abstract void addDropsOnDeath(List<ItemStack> nameList);

    public abstract int getXPOnDeath();

    public int addUpParts()
    {
        return this.droneInfo.casing + this.droneInfo.chip + this.droneInfo.core + this.droneInfo.engine;
    }

    public static ItemDronePart getPart(String s, int i)
    {
        if (s.equalsIgnoreCase("casing")) {
            switch (i)
            {
                case 1:
                    return CustomDrones.case1;
                case 2:
                    return CustomDrones.case2;
                case 3:
                    return CustomDrones.case3;
                case 4:
                    return CustomDrones.case4;
            }
        } else if (s.equalsIgnoreCase("chip")) {
            switch (i)
            {
                case 1:
                    return CustomDrones.chip1;
                case 2:
                    return CustomDrones.chip2;
                case 3:
                    return CustomDrones.chip3;
                case 4:
                    return CustomDrones.chip4;
            }
        } else if (s.equalsIgnoreCase("core")) {
            switch (i)
            {
                case 1:
                    return CustomDrones.core1;
                case 2:
                    return CustomDrones.core2;
                case 3:
                    return CustomDrones.core3;
                case 4:
                    return CustomDrones.core4;
            }
        } else if (s.equalsIgnoreCase("engine")) {
            switch (i)
            {
                case 1:
                    return CustomDrones.engine1;
                case 2:
                    return CustomDrones.engine2;
                case 3:
                    return CustomDrones.engine3;
                case 4:
                    return CustomDrones.engine4;
            }
        }
        return null;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Hostile", this.hostile);
        tagCompound.setBoolean("Despawn", this.shouldDespawn);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);
        this.hostile = tagCompound.getBoolean("Hostile");
        this.shouldDespawn = tagCompound.getBoolean("Despawn");
    }
}

