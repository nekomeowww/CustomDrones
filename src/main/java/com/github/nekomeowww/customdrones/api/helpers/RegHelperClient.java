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
        Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178086_a(item, 0, new ModelResourceLocation("drones:" + item
                .func_77658_a().substring(5), "inventory"));
    }

    public static void registerBlockRenderer(Block block)
    {
        Item item = Item.func_150898_a(block);
        Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178086_a(item, 0, new ModelResourceLocation("drones:" + item
                .func_77658_a().substring(5), "inventory"));
    }

    public static void registerEntityRenderer(Class<? extends Entity> entityClass)
    {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new RenderMaker(entityClass));
    }
}