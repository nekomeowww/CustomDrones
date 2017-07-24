package com.github.nekomeowww.customdrones;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import com.github.nekomeowww.customdrones.api.helpers.RegHelperClient;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.entity.EntityDroneBaby;
import com.github.nekomeowww.customdrones.entity.EntityDroneBabyBig;
import com.github.nekomeowww.customdrones.entity.EntityDroneWildItem;
import com.github.nekomeowww.customdrones.entity.EntityHomingBox;
import com.github.nekomeowww.customdrones.entity.EntityPlasmaShot;
import com.github.nekomeowww.customdrones.handlers.ClientEventHandler;

public class ClientProxy
        extends CommonProxy
{
    public void postLoad(FMLPostInitializationEvent event)
    {
        super.postLoad(event);
        com.github.nekomeowww.customdrones.api.model.CMBase.USECOLORSPRITE = FMLClientHandler.instance().hasOptifine();
    }

    public void makeColorSpriteFile(File file)
    {
        int colorWidth = 24;
        BufferedImage bi = new BufferedImage(colorWidth * colorWidth, colorWidth * colorWidth, 2);

        WritableRaster raster = (WritableRaster)bi.getData();
        for (int a = 0; a < colorWidth; a++) {
            for (int b = 0; b < colorWidth; b++) {
                for (int g = 0; g < colorWidth; g++) {
                    for (int r = 0; r < colorWidth; r++)
                    {
                        Color c = new Color(r / colorWidth, g / colorWidth, b / colorWidth, a / colorWidth);

                        bi.setRGB(b * colorWidth + r, a * colorWidth + g, c.toInt());
                    }
                }
            }
        }
        file.mkdirs();
        try
        {
            ImageIO.write(bi, "png", file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void registerEntityRenders()
    {
        RegHelperClient.registerEntityRenderer(EntityDrone.class);
        RegHelperClient.registerEntityRenderer(EntityDroneBaby.class);
        RegHelperClient.registerEntityRenderer(EntityDroneBabyBig.class);
        RegHelperClient.registerEntityRenderer(EntityPlasmaShot.class);
        RegHelperClient.registerEntityRenderer(EntityHomingBox.class);
        RegHelperClient.registerEntityRenderer(EntityDroneWildItem.class);
    }

    public void registerStuffRenders()
    {
        registerItemRender(CustomDrones.droneSpawn, 0);
        registerItemRender(CustomDrones.droneFlyer, 0);
        registerItemRender(CustomDrones.dronePainter, 0);
        registerItemRender(CustomDrones.droneScrew, 0);
        for (int a = 0; a < 16; a++) {
            registerItemRender(CustomDrones.droneBit, a);
        }
        registerItemRender(CustomDrones.cfPlate1, 0);
        registerItemRender(CustomDrones.cfPlate2, 0);
        registerItemRender(CustomDrones.cfPlate3, 0);
        registerItemRender(CustomDrones.cfPlate4, 0);
        registerItemRender(CustomDrones.chip1, 0);
        registerItemRender(CustomDrones.chip2, 0);
        registerItemRender(CustomDrones.chip3, 0);
        registerItemRender(CustomDrones.chip4, 0);
        registerItemRender(CustomDrones.core1, 0);
        registerItemRender(CustomDrones.core2, 0);
        registerItemRender(CustomDrones.core3, 0);
        registerItemRender(CustomDrones.core4, 0);
        registerItemRender(CustomDrones.engine1, 0);
        registerItemRender(CustomDrones.engine2, 0);
        registerItemRender(CustomDrones.engine3, 0);
        registerItemRender(CustomDrones.engine4, 0);
        registerItemRender(CustomDrones.case1, 0);
        registerItemRender(CustomDrones.case2, 0);
        registerItemRender(CustomDrones.case3, 0);
        registerItemRender(CustomDrones.case4, 0);
        for (int a = 0; a < 4; a++)
        {
            ModelResourceLocation mrl;
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(CustomDrones.droneModule, a,
                    mrl = itemModelResLoc(CustomDrones.droneModule, a + 1 + ""));
            ModelLoader.setCustomModelResourceLocation(CustomDrones.droneModule, a, mrl);
        }
        registerBlockRender(CustomDrones.crafter);
        registerItemRender(CustomDrones.plasmaGun, 0);
        registerItemRender(CustomDrones.plasmaGunHoming, 0);
        registerItemRender(CustomDrones.gunUpgrade, 0);
    }

    public void registerHandlers()
    {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    public void registerItemRender(Item item, int meta)
    {
        RenderItem ri = Minecraft.getMinecraft().getRenderItem();
        ri.getItemModelMesher().register(item, meta, itemModelResLoc(item, ""));
    }

    public void registerBlockRender(Block block)
    {
        Item itemBlockSimple = ItemBlock.getItemFromBlock(block);
        RenderItem ri = Minecraft.getMinecraft().getRenderItem();
        ri.getItemModelMesher().register(itemBlockSimple, 0, itemModelResLoc(itemBlockSimple, ""));
    }

    public ModelResourceLocation itemModelResLoc(Item item, String add)
    {
        return new ModelResourceLocation(item.getRegistryName() + add, "inventory");
    }

    public ModelResourceLocation blockModelResLoc(Block block, String add)
    {
        return new ModelResourceLocation(block.getRegistryName() + add, "inventory");
    }

    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        return ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx);
    }

    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }
}
