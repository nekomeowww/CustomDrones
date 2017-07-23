package com.github.nekomeowww.customdrones.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDroneBit
        extends Item
{
    public boolean func_77636_d(ItemStack stack)
    {
        return false;
    }

    public boolean func_77614_k()
    {
        return true;
    }

    public EnumRarity func_77613_e(ItemStack stack)
    {
        if (stack.func_77952_i() == 1) {
            return EnumRarity.RARE;
        }
        return super.func_77613_e(stack);
    }
}
