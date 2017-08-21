package com.github.nekomeowww.customdrones;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.github.nekomeowww.customdrones.common.CommonProxy;

/*********************************************************************
 * Code Re-written by NekoMeowww
 * Original Author WilliamEz
 *********************************************************************/

@Mod(modid = CustomDrones.MODID, name = CustomDrones.NAME, version = CustomDrones.VERSION, acceptedMinecraftVersions = "[1.10.2,)")
public class CustomDrones
{
    public static final String MODID = "customdrones";
    public static final String NAME = "CustomDrones";
    public static final String VERSION = "2.0.0";

    @Instance(CustomDrones.MODID)
    public static CustomDrones instance;

    @SidedProxy(clientSide = "com.github.nekomeowww.customdrones.client.ClientProxy",
            serverSide = "com.github.nekomeowww.customdrones.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
