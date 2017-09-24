package com.github.nekomeowww.customdrones.item;

import com.github.nekomeowww.customdrones.creativetab.CreativeTabsLoader;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemLoader
{
    public static ItemDronePart cfPlate1 = (ItemDronePart)new
            ItemDronePart(56, 1).setCreativeTab(CreativeTabsLoader.tabCustomDrones).setUnlocalizedName("cfPlate1");

    public ItemLoader(FMLPreInitializationEvent event)
    {
        register(cfPlate1, "cfplate1");
    }

    private static void register(Item item, String name)
    {
        //Needs to be updated to be compatible with 1.11 and 1.12
        GameRegistry.registerItem(item.setRegistryName(name));
    }
}
