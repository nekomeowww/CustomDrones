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
        this.field_98038_p = true;
        func_70105_a(1.0F, 1.0F);
        this.field_70145_X = true;
    }

    public void func_70071_h_()
    {
        super.func_70071_h_();
        if (this.target != null)
        {
            func_70105_a(this.target.field_70130_N, this.target.field_70131_O);
            func_70107_b(this.target.field_70165_t, this.target.field_70163_u, this.target.field_70161_v);
        }
        else
        {
            func_70105_a(1.0F, 1.0F);
        }
    }

    protected void func_70088_a() {}

    public boolean func_70067_L()
    {
        return false;
    }

    public boolean func_70104_M()
    {
        return false;
    }

    protected boolean func_70041_e_()
    {
        return false;
    }

    public boolean canRiderInteract()
    {
        return false;
    }

    protected void func_70037_a(NBTTagCompound compound) {}

    protected void func_70014_b(NBTTagCompound compound) {}
}
