package com.github.nekomeowww.customdrones.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CreativeTabsLoader
{
    public static CreativeTabs tabCustomDrones;

    public CreativeTabsLoader(FMLPreInitializationEvent event)
    {
        tabCustomDrones = new CreativeTabsCustomDrones();
    }
}
