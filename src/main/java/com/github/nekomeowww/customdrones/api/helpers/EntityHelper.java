package com.github.nekomeowww.customdrones.api.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;

public class EntityHelper
{
    public static void addChat(EntityPlayer p, int worldSide, String chat)
    {
        if ((worldSide == 0) || ((worldSide == 1) && ((p instanceof EntityPlayerMP))) || ((worldSide == 2) && ((p instanceof EntityPlayerMP)))) {
            p.func_146105_b(new TextComponentString(chat));
        }
    }

    public static ItemStack addItemStackToInv(IInventory inv, ItemStack is)
    {
        int remaining = is.field_77994_a;
        for (int a = 0; a < inv.func_70302_i_(); a++)
        {
            ItemStack is0 = inv.func_70301_a(a);
            int toAdd = 0;
            if (is0 == null)
            {
                toAdd = Math.min(Math.min(is.field_77994_a, is.func_77976_d()), inv.func_70297_j_());
                ItemStack copy = is.func_77946_l();
                copy.field_77994_a = toAdd;
                inv.func_70299_a(a, copy);
            }
            else if ((ItemStack.func_179545_c(is0, is)) && (ItemStack.func_77970_a(is0, is)))
            {
                int isAllow = is0.func_77976_d() - is0.field_77994_a;
                int invAllow = inv.func_70297_j_() - is0.field_77994_a;
                toAdd = Math.min(Math.min(is.field_77994_a, isAllow), invAllow);
                is0.field_77994_a += toAdd;
            }
            is.field_77994_a -= toAdd;
            if (is.field_77994_a <= 0)
            {
                is = null;
                break;
            }
        }
        inv.func_70296_d();
        return is;
    }

    public static Vec3d getCenterVec(Entity e)
    {
        if (e != null)
        {
            AxisAlignedBB aabb = e.func_174813_aQ();
            if (aabb != null) {
                return new Vec3d((aabb.field_72336_d + aabb.field_72340_a) / 2.0D, (aabb.field_72337_e + aabb.field_72338_b) / 2.0D, (aabb.field_72334_f + aabb.field_72339_c) / 2.0D);
            }
            return e.func_174791_d().func_72441_c(0.0D, e.field_70131_O / 2.0F, 0.0D);
        }
        return null;
    }

    public static Vec3d getEyeVec(Entity e)
    {
        return e.func_174791_d().func_72441_c(0.0D, e.func_70047_e(), 0.0D);
    }
}

