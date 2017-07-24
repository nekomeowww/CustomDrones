package com.github.nekomeowww.customdrones.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDroneBit
        extends Item
{
    public boolean hasEffect(ItemStack stack)
    {
        return false;
    }

    public boolean getHasSubtypes()
    {
        return true;
    }

    public EnumRarity getRarity(ItemStack stack)
    {
        if (stack.getItemDamage() == 1) {
            return EnumRarity.RARE;
        }
        return super.getRarity(stack);
    }
}
