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
    public Entity field_110150_bn;
    public boolean hostile;
    public boolean shouldDespawn;

    public EntityDroneMob(World worldIn)
    {
        super(worldIn);
        this.modelScale = 1.0D;
        this.droneTasks = new EntityAITasks((worldIn != null) && (worldIn.field_72984_F != null) ? worldIn.field_72984_F : null);
        if ((worldIn != null) && (!worldIn.field_72995_K)) {
            initDroneAI();
        }
    }

    protected boolean func_70692_ba()
    {
        return this.shouldDespawn;
    }

    public IEntityLivingData func_180482_a(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        onInitSpawn();
        return super.func_180482_a(difficulty, livingdata);
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
        this.droneInfo.casing = (4 - (int)Math.floor(Math.log(this.field_70146_Z.nextInt(15) + 1) / log2));
        this.droneInfo.chip = (4 - (int)Math.floor(Math.log(this.field_70146_Z.nextInt(15) + 1) / log2));
        this.droneInfo.core = (4 - (int)Math.floor(Math.log(this.field_70146_Z.nextInt(15) + 1) / log2));
        this.droneInfo.engine = (4 - (int)Math.floor(Math.log(this.field_70146_Z.nextInt(15) + 1) / log2));
    }

    public void setAppearanceBasedOnBiome()
    {
        Biome biome = this.field_70170_p.getBiomeForCoordsBody(func_180425_c());
        if (((biome instanceof BiomeSnow)) || (this.field_70170_p.canSnowAtBody(func_180425_c(), false))) {
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

    public void func_70030_z()
    {
        super.func_70030_z();
        this.droneTasks.func_75774_a();
    }

    public boolean func_70097_a(DamageSource source, float amount)
    {
        if (source != null) {
            this.field_110150_bn = source.func_76346_g();
        }
        return super.func_70097_a(source, amount);
    }

    public float func_70047_e()
    {
        return this.field_70131_O / 2.0F;
    }

    public int getFlyingMode()
    {
        return this.MOBID;
    }

    public void func_70106_y()
    {
        super.func_70106_y();
        if ((!this.field_70170_p.field_72995_K) && (this.field_70170_p.func_82736_K().func_82766_b("doMobLoot")))
        {
            List<ItemStack> itemsToDrop = new ArrayList();
            addDropsOnDeath(itemsToDrop);
            for (ItemStack is : itemsToDrop) {
                func_70099_a(is, this.field_70146_Z.nextFloat() * this.field_70131_O);
            }
            if (((this.field_110150_bn instanceof EntityPlayer)) || (((this.field_110150_bn instanceof EntityDrone)) &&
                    (((EntityDrone)this.field_110150_bn).getControllingPlayer() != null)))
            {
                int i = getXPOnDeath();
                while (i > 0)
                {
                    int j = EntityXPOrb.func_70527_a(i);
                    i -= j;
                    this.field_70170_p.func_72838_d(new EntityXPOrb(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, j));
                }
            }
        }
    }

    public abstract void addDropsOnDeath(List<ItemStack> paramList);

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
                    return DronesMod.case1;
                case 2:
                    return DronesMod.case2;
                case 3:
                    return DronesMod.case3;
                case 4:
                    return DronesMod.case4;
            }
        } else if (s.equalsIgnoreCase("chip")) {
            switch (i)
            {
                case 1:
                    return DronesMod.chip1;
                case 2:
                    return DronesMod.chip2;
                case 3:
                    return DronesMod.chip3;
                case 4:
                    return DronesMod.chip4;
            }
        } else if (s.equalsIgnoreCase("core")) {
            switch (i)
            {
                case 1:
                    return DronesMod.core1;
                case 2:
                    return DronesMod.core2;
                case 3:
                    return DronesMod.core3;
                case 4:
                    return DronesMod.core4;
            }
        } else if (s.equalsIgnoreCase("engine")) {
            switch (i)
            {
                case 1:
                    return DronesMod.engine1;
                case 2:
                    return DronesMod.engine2;
                case 3:
                    return DronesMod.engine3;
                case 4:
                    return DronesMod.engine4;
            }
        }
        return null;
    }

    public void func_70014_b(NBTTagCompound tagCompound)
    {
        super.func_70014_b(tagCompound);
        tagCompound.func_74757_a("Hostile", this.hostile);
        tagCompound.func_74757_a("Despawn", this.shouldDespawn);
    }

    public void func_70037_a(NBTTagCompound tagCompound)
    {
        super.func_70037_a(tagCompound);
        this.hostile = tagCompound.func_74767_n("Hostile");
        this.shouldDespawn = tagCompound.func_74767_n("Despawn");
    }
}

