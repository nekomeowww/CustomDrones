package com.github.nekomeowww.customdrones.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import com.github.nekomeowww.customdrones.item.ItemLoader;

import com.github.nekomeowww.customdrones.creativetab.CreativeTabsLoader;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        new ItemLoader(event);
        new CreativeTabsLoader(event);
    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
