package com.github.nekomeowww.customdrones.api.helpers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import com.github.nekomeowww.customdrones.render.RenderMaker;

public class RegHelperClient
{
    public static void registerItemRenderer(Item item)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("drones:" + item
                .getUnlocalizedName().substring(5), "inventory"));
    }

    public static void registerBlockRenderer(Block block)
    {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("drones:" + item
                .getUnlocalizedName().substring(5), "inventory"));
    }

    public static void registerEntityRenderer(Class<? extends Entity> entityClass)
    {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new RenderMaker(entityClass));
    }
}