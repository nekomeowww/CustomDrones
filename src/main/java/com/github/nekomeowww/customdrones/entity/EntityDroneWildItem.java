package com.github.nekomeowww.customdrones.entity;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.DroneWeightedLists;
import com.github.nekomeowww.customdrones.drone.DroneWeightedLists.WeightedItemStack;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.entity.ai.DroneAIFlyToNearest;
import com.github.nekomeowww.customdrones.entity.ai.DroneAIWander;
import com.github.nekomeowww.customdrones.render.DroneModels;
import com.github.nekomeowww.customdrones.render.DroneModels.ModelProp;

public class EntityDroneWildItem
        extends EntityDroneMob
{
    public ItemStack holding;

    public EntityDroneWildItem(World worldIn)
    {
        super(worldIn);
        setSize(0.5F, 0.7F);
        this.modelScale = 0.5D;
    }

    public void onInitSpawn()
    {
        while (this.holding == null)
        {
            DroneWeightedLists.WeightedItemStack wis = (DroneWeightedLists.WeightedItemStack)WeightedRandom.getRandomItem(this.rand, DroneWeightedLists.wildHoldingList);
            if ((wis != null) && (wis.is != null))
            {
                this.holding = wis.is.copy();
                if (((this.holding.getItem() instanceof ItemBlock)) && (((ItemBlock)this.holding.getItem()).block == Blocks.TNT)) {
                    this.droneTasks.addTask(1, new DroneAIFlyToNearest(this, 16.0D, 0.75D, EntityPlayer.class));
                }
            }
        }
        super.onInitSpawn();
    }

    public void initDroneInfo()
    {
        if ((this.droneInfo.casing == 1) && (this.droneInfo.chip == 1) && (this.droneInfo.core == 1) && (this.droneInfo.engine == 1)) {
            randomizeDroneParts();
        }
        super.initDroneInfo();
    }

    public void initSpawnSetAppearance()
    {
        //Debug
        /*
        if(DroneModels.instance == null)
        {
            System.out.println("Instance Error");
            return;
        }
        */
        this.droneInfo.appearance.modelID = DroneModels.instance.wildItem.id;
        super.initSpawnSetAppearance();
    }

    public void initSpawnAddModules()
    {
        this.droneInfo.mods.clear();
        this.droneInfo.applyModule(Module.getModule("Battery Saving " + DroneInfo.greekNumber[this.droneInfo.chip]));
        this.droneInfo.applyModule(Module.getModule("Armor " + DroneInfo.greekNumber[this.droneInfo.casing]));
        this.droneInfo.applyModule(Module.multiMovement);
    }

    public void initDroneAI()
    {
        this.droneTasks.addTask(10, new DroneAIWander(this, 16.0D, 0.5D));
    }

    public void initDroneAIPostSpawn() {}

    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        super.onCollideWithPlayer(entityIn);
        if ((this.holding != null) && ((this.holding.getItem() instanceof ItemBlock)) &&
                (((ItemBlock)this.holding.getItem()).block == Blocks.TNT))
        {
            this.holding = null;
            this.getEntityWorld().createExplosion(this, this.posX, this.posY, this.posZ, 0.5F * this.droneInfo.core, true);
            setDead();
        }
    }

    public void updateWingRotation()
    {
        super.updateWingRotation();
    }

    public void addDropsOnDeath(List<ItemStack> list)
    {
        if (this.holding != null) {
            list.add(this.holding);
        }
    }

    public int getXPOnDeath()
    {
        return 2;
    }

    public void writeSpawnData(ByteBuf buffer)
    {
        super.writeSpawnData(buffer);
        buffer.writeBoolean(this.holding != null);
        if (this.holding != null) {
            ByteBufUtils.writeItemStack(buffer, this.holding);
        }
    }

    public void readSpawnData(ByteBuf additionalData)
    {
        super.readSpawnData(additionalData);
        if (additionalData.readBoolean()) {
            this.holding = ByteBufUtils.readItemStack(additionalData);
        }
    }

    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);
        tag.setBoolean("Holding", this.holding != null);
        if (this.holding != null)
        {
            NBTTagCompound holdingTag = new NBTTagCompound();
            this.holding.writeToNBT(holdingTag);
            tag.setTag("Holding tag", holdingTag);
        }
    }

    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);
        if (tag.getBoolean("Holding"))
        {
            NBTTagCompound holdingTag = tag.getCompoundTag("Holding tag");
            this.holding = ItemStack.loadItemStackFromNBT(holdingTag);
        }
        this.droneInfo.appearance.modelID = DroneModels.instance.wildItem.id;
    }
}
