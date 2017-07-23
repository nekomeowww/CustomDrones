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
        func_70105_a(0.5F, 0.7F);
        this.modelScale = 0.5D;
    }

    public void onInitSpawn()
    {
        while (this.holding == null)
        {
            DroneWeightedLists.WeightedItemStack wis = (DroneWeightedLists.WeightedItemStack)WeightedRandom.func_76271_a(this.field_70146_Z, DroneWeightedLists.wildHoldingList);
            if ((wis != null) && (wis.is != null))
            {
                this.holding = wis.is.func_77946_l();
                if (((this.holding.func_77973_b() instanceof ItemBlock)) && (((ItemBlock)this.holding.func_77973_b()).field_150939_a == Blocks.field_150335_W)) {
                    this.droneTasks.func_75776_a(1, new DroneAIFlyToNearest(this, 16.0D, 0.75D, EntityPlayer.class));
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
        this.droneTasks.func_75776_a(10, new DroneAIWander(this, 16.0D, 0.5D));
    }

    public void initDroneAIPostSpawn() {}

    public void func_70100_b_(EntityPlayer entityIn)
    {
        super.func_70100_b_(entityIn);
        if ((this.holding != null) && ((this.holding.func_77973_b() instanceof ItemBlock)) &&
                (((ItemBlock)this.holding.func_77973_b()).field_150939_a == Blocks.field_150335_W))
        {
            this.holding = null;
            this.field_70170_p.func_72876_a(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.5F * this.droneInfo.core, true);
            func_70106_y();
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

    public void func_70014_b(NBTTagCompound tag)
    {
        super.func_70014_b(tag);
        tag.func_74757_a("Holding", this.holding != null);
        if (this.holding != null)
        {
            NBTTagCompound holdingTag = new NBTTagCompound();
            this.holding.func_77955_b(holdingTag);
            tag.func_74782_a("Holding tag", holdingTag);
        }
    }

    public void func_70037_a(NBTTagCompound tag)
    {
        super.func_70037_a(tag);
        if (tag.func_74767_n("Holding"))
        {
            NBTTagCompound holdingTag = tag.func_74775_l("Holding tag");
            this.holding = ItemStack.func_77949_a(holdingTag);
        }
        this.droneInfo.appearance.modelID = DroneModels.instance.wildItem.id;
    }
}
