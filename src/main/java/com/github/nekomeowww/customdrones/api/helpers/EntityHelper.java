package com.github.nekomeowww.customdrones.api.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class EntityHelper
{

    public static void addChat(EntityPlayer p, int worldSide, String chat)
    {
        if ((worldSide == 0) || ((worldSide == 1) && ((p instanceof EntityPlayerMP))) || ((worldSide == 2) && ((p instanceof EntityPlayerMP)))) {
            p.addChatComponentMessage(new TextComponentString(chat));
        }
    }

    public static ItemStack addItemStackToInv(IInventory inv, ItemStack is)
    {
        int remaining = is.stackSize;
        for (int a = 0; a < inv.getSizeInventory(); a++)
        {
            ItemStack is0 = inv.getStackInSlot(a);
            int toAdd = 0;
            if (is0 == null)
            {
                toAdd = Math.min(Math.min(is.stackSize, is.getMaxStackSize()), inv.getInventoryStackLimit());
                ItemStack copy = is.copy();
                copy.stackSize = toAdd;
                inv.setInventorySlotContents(a, copy);
            }
            else if ((ItemStack.areItemsEqual(is0, is)) && (ItemStack.areItemStackTagsEqual(is0, is)))
            {
                int isAllow = is0.getMaxStackSize() - is0.stackSize;
                int invAllow = inv.getInventoryStackLimit() - is0.stackSize;
                toAdd = Math.min(Math.min(is.stackSize, isAllow), invAllow);
                is0.stackSize += toAdd;
            }
            is.stackSize -= toAdd;
            if (is.stackSize <= 0)
            {
                is = null;
                break;
            }
        }
        inv.markDirty();
        return is;
    }

    public static Vec3d getCenterVec(Entity e)
    {
        if (e != null)
        {
            AxisAlignedBB aabb = e.getEntityBoundingBox();
            if (aabb != null) {
                return new Vec3d((aabb.maxX + aabb.minX) / 2.0D, (aabb.maxY + aabb.minY) / 2.0D, (aabb.maxZ + aabb.minZ) / 2.0D);
            }
            return e.getPositionVector().addVector(0.0D, e.height / 2.0F, 0.0D);
        }
        return null;
    }

    public static Vec3d getEyeVec(Entity e)
    {
        return e.getPositionVector().addVector(0.0D, e.getEyeHeight(), 0.0D);
    }
}

