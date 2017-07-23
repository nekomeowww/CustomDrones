package com.github.nekomeowww.customdrones.item;

import net.minecraft.item.Item;

public class ItemDronePart
        extends Item
{
    public int rank;

    public ItemDronePart(int i, int rank)
    {
        func_77625_d(i);
        this.rank = rank;
    }

    public ItemDronePart(int rank)
    {
        this.rank = rank;
    }
}
