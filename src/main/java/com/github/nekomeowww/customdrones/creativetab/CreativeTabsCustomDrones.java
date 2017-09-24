package com.github.nekomeowww.customdrones.creativetab;

import com.github.nekomeowww.customdrones.item.ItemLoader;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabsCustomDrones extends CreativeTabs
{
    public CreativeTabsCustomDrones()
    {
        super("customdrones");
    }

    @Override
    public Item getTabIconItem()
    {
        return ItemLoader.cfPlate1;
    }
}
