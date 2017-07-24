package com.github.nekomeowww.customdrones.api.helpers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegHelper
{
    public static void registerBlock(Block block)
    {
        GameRegistry.register(block.setRegistryName("drones", block.getUnlocalizedName().substring(5)));
        GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    public static void registerItem(Item item)
    {
        GameRegistry.register(item.setRegistryName("drones", item.getUnlocalizedName().substring(5)));
    }

    public static void registerEntity(Class entityClass, String entityName, int id, Object mod, int range, int freq, boolean velo, int... egg)
    {
        EntityRegistry.registerModEntity(entityClass, entityName, id, mod, range, freq, velo);
        if (egg.length >= 2) {
            EntityRegistry.registerEgg(entityClass, egg[0], egg[1]);
        }
    }
}
