package com.github.nekomeowww.customdrones.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityHomingBox
        extends Entity
{
    public Entity target;

    public EntityHomingBox(World worldIn)
    {
        super(worldIn);
        this.forceSpawn = true;
        setSize(1.0F, 1.0F);
        this.noClip = true;
    }

    public void onUpdate()
    {
        super.onUpdate();
        if (this.target != null)
        {
            setSize(this.target.width, this.target.height);
            setPosition(this.target.posX, this.target.posY, this.target.posZ);
        }
        else
        {
            setSize(1.0F, 1.0F);
        }
    }

    protected void entityInit() {}

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public boolean canBePushed()
    {
        return false;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public boolean canRiderInteract()
    {
        return false;
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {}

    protected void writeEntityToNBT(NBTTagCompound compound) {}
}
