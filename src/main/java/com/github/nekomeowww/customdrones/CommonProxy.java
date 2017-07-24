package com.github.nekomeowww.customdrones;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import com.github.nekomeowww.customdrones.drone.DroneAppearance.ColorPalette;
import com.github.nekomeowww.customdrones.gui.GuiHandler;
import com.github.nekomeowww.customdrones.handlers.EventHandler;
import com.github.nekomeowww.customdrones.worldgen.WorldGen;

public class CommonProxy
{
    public void preLoad(FMLPreInitializationEvent event)
    {
        registerEntityRenders();
        DroneAppearance.loadPresetPalettes();
        File modFolder = getDronesFolder(event);
        modFolder.mkdirs();
        for (File file : modFolder.listFiles()) {
            if (file.getName().endsWith(".palette")) {
                DroneAppearance.loadPaletteFile(file);
            }
        }
        for (Iterator iter = DroneAppearance.presetPalettes.iterator(); iter.hasNext();)
        {
            DroneAppearance.ColorPalette palette = (DroneAppearance.ColorPalette) iter.next();

            File paletteFile = new File(modFolder, palette.paletteName + ".palette");
            palette.savePaletteFile(paletteFile);
        }
    }

    public File getDronesFolder(FMLPreInitializationEvent event)
    {
        return new File(event.getModConfigurationDirectory(), "/customdrones/");
    }

    public void load(FMLInitializationEvent event) {}

    public void postLoad(FMLPostInitializationEvent event) {}

    public void registerEntityRenders() {}

    public void registerStuffRenders() {}

    public void registerHandlers()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(CustomDrones.instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        GameRegistry.registerWorldGenerator(new WorldGen(), 10);
    }

    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        return ctx.getServerHandler().playerEntity;
    }

    public EntityPlayer getClientPlayer()
    {
        return null;
    }
}
