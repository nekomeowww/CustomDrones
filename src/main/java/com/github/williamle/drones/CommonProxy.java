/*    */ package williamle.drones;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*    */ import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
/*    */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.EventBus;
/*    */ import net.minecraftforge.fml.common.network.NetworkRegistry;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.drone.DroneAppearance;
/*    */ import williamle.drones.drone.DroneAppearance.ColorPalette;
/*    */ import williamle.drones.gui.GuiHandler;
/*    */ import williamle.drones.handlers.EventHandler;
/*    */ import williamle.drones.worldgen.WorldGen;
/*    */ 
/*    */ public class CommonProxy
/*    */ {
/*    */   public void preLoad(FMLPreInitializationEvent event)
/*    */   {
/* 23 */     registerEntityRenders();
/* 24 */     DroneAppearance.loadPresetPalettes();
/* 25 */     File modFolder = getDronesFolder(event);
/* 26 */     modFolder.mkdirs();
/* 27 */     for (File file : modFolder.listFiles())
/*    */     {
/* 29 */       if (file.getName().endsWith(".palette"))
/*    */       {
/* 31 */         DroneAppearance.loadPaletteFile(file);
/*    */       }
/*    */     }
/* 34 */     for (??? = DroneAppearance.presetPalettes.iterator(); ((Iterator)???).hasNext();) { DroneAppearance.ColorPalette palette = (DroneAppearance.ColorPalette)((Iterator)???).next();
/*    */       
/* 36 */       File paletteFile = new File(modFolder, palette.paletteName + ".palette");
/* 37 */       palette.savePaletteFile(paletteFile);
/*    */     }
/*    */   }
/*    */   
/*    */   public File getDronesFolder(FMLPreInitializationEvent event)
/*    */   {
/* 43 */     return new File(event.getModConfigurationDirectory(), "/customdrones/");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void load(FMLInitializationEvent event) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void postLoad(FMLPostInitializationEvent event) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void registerEntityRenders() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void registerStuffRenders() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void registerHandlers()
/*    */   {
/* 67 */     NetworkRegistry.INSTANCE.registerGuiHandler(DronesMod.instance, new GuiHandler());
/* 68 */     MinecraftForge.EVENT_BUS.register(new EventHandler());
/* 69 */     net.minecraftforge.fml.common.registry.GameRegistry.registerWorldGenerator(new WorldGen(), 10);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public EntityPlayer getPlayerEntity(MessageContext ctx)
/*    */   {
/* 76 */     return ctx.getServerHandler().field_147369_b;
/*    */   }
/*    */   
/*    */   public EntityPlayer getClientPlayer()
/*    */   {
/* 81 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\CommonProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */