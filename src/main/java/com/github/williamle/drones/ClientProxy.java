/*     */ package williamle.drones;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.ItemModelMesher;
/*     */ import net.minecraft.client.renderer.RenderItem;
/*     */ import net.minecraft.client.renderer.block.model.ModelResourceLocation;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraftforge.client.model.ModelLoader;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.client.FMLClientHandler;
/*     */ import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventBus;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import williamle.drones.api.helpers.RegHelperClient;
/*     */ import williamle.drones.api.model.Color;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.entity.EntityDroneBaby;
/*     */ import williamle.drones.entity.EntityDroneBabyBig;
/*     */ import williamle.drones.entity.EntityDroneWildItem;
/*     */ import williamle.drones.entity.EntityHomingBox;
/*     */ import williamle.drones.entity.EntityPlasmaShot;
/*     */ import williamle.drones.handlers.ClientEventHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientProxy
/*     */   extends CommonProxy
/*     */ {
/*     */   public void postLoad(FMLPostInitializationEvent event)
/*     */   {
/*  41 */     super.postLoad(event);
/*  42 */     williamle.drones.api.model.CMBase.USECOLORSPRITE = FMLClientHandler.instance().hasOptifine();
/*     */   }
/*     */   
/*     */ 
/*     */   public void makeColorSpriteFile(File file)
/*     */   {
/*  48 */     int colorWidth = 24;
/*  49 */     BufferedImage bi = new BufferedImage(colorWidth * colorWidth, colorWidth * colorWidth, 2);
/*     */     
/*  51 */     WritableRaster raster = (WritableRaster)bi.getData();
/*  52 */     for (int a = 0; a < colorWidth; a++)
/*     */     {
/*  54 */       for (int b = 0; b < colorWidth; b++)
/*     */       {
/*  56 */         for (int g = 0; g < colorWidth; g++)
/*     */         {
/*  58 */           for (int r = 0; r < colorWidth; r++)
/*     */           {
/*  60 */             Color c = new Color(r / colorWidth, g / colorWidth, b / colorWidth, a / colorWidth);
/*     */             
/*  62 */             bi.setRGB(b * colorWidth + r, a * colorWidth + g, c.toInt());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  67 */     file.mkdirs();
/*     */     try
/*     */     {
/*  70 */       ImageIO.write(bi, "png", file);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  74 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void registerEntityRenders()
/*     */   {
/*  81 */     RegHelperClient.registerEntityRenderer(EntityDrone.class);
/*  82 */     RegHelperClient.registerEntityRenderer(EntityDroneBaby.class);
/*  83 */     RegHelperClient.registerEntityRenderer(EntityDroneBabyBig.class);
/*  84 */     RegHelperClient.registerEntityRenderer(EntityPlasmaShot.class);
/*  85 */     RegHelperClient.registerEntityRenderer(EntityHomingBox.class);
/*  86 */     RegHelperClient.registerEntityRenderer(EntityDroneWildItem.class);
/*     */   }
/*     */   
/*     */ 
/*     */   public void registerStuffRenders()
/*     */   {
/*  92 */     registerItemRender(DronesMod.droneSpawn, 0);
/*  93 */     registerItemRender(DronesMod.droneFlyer, 0);
/*  94 */     registerItemRender(DronesMod.dronePainter, 0);
/*  95 */     registerItemRender(DronesMod.droneScrew, 0);
/*  96 */     for (int a = 0; a < 16; a++)
/*     */     {
/*  98 */       registerItemRender(DronesMod.droneBit, a);
/*     */     }
/* 100 */     registerItemRender(DronesMod.cfPlate1, 0);
/* 101 */     registerItemRender(DronesMod.cfPlate2, 0);
/* 102 */     registerItemRender(DronesMod.cfPlate3, 0);
/* 103 */     registerItemRender(DronesMod.cfPlate4, 0);
/* 104 */     registerItemRender(DronesMod.chip1, 0);
/* 105 */     registerItemRender(DronesMod.chip2, 0);
/* 106 */     registerItemRender(DronesMod.chip3, 0);
/* 107 */     registerItemRender(DronesMod.chip4, 0);
/* 108 */     registerItemRender(DronesMod.core1, 0);
/* 109 */     registerItemRender(DronesMod.core2, 0);
/* 110 */     registerItemRender(DronesMod.core3, 0);
/* 111 */     registerItemRender(DronesMod.core4, 0);
/* 112 */     registerItemRender(DronesMod.engine1, 0);
/* 113 */     registerItemRender(DronesMod.engine2, 0);
/* 114 */     registerItemRender(DronesMod.engine3, 0);
/* 115 */     registerItemRender(DronesMod.engine4, 0);
/* 116 */     registerItemRender(DronesMod.case1, 0);
/* 117 */     registerItemRender(DronesMod.case2, 0);
/* 118 */     registerItemRender(DronesMod.case3, 0);
/* 119 */     registerItemRender(DronesMod.case4, 0);
/* 120 */     for (int a = 0; a < 4; a++)
/*     */     {
/*     */       ModelResourceLocation mrl;
/* 123 */       Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178086_a(DronesMod.droneModule, a, 
/* 124 */         mrl = itemModelResLoc(DronesMod.droneModule, a + 1 + ""));
/* 125 */       ModelLoader.setCustomModelResourceLocation(DronesMod.droneModule, a, mrl);
/*     */     }
/* 127 */     registerBlockRender(DronesMod.crafter);
/* 128 */     registerItemRender(DronesMod.plasmaGun, 0);
/* 129 */     registerItemRender(DronesMod.plasmaGunHoming, 0);
/* 130 */     registerItemRender(DronesMod.gunUpgrade, 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public void registerHandlers()
/*     */   {
/* 136 */     super.registerHandlers();
/* 137 */     MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
/*     */   }
/*     */   
/*     */   public void registerItemRender(Item item, int meta)
/*     */   {
/* 142 */     RenderItem ri = Minecraft.func_71410_x().func_175599_af();
/* 143 */     ri.func_175037_a().func_178086_a(item, meta, itemModelResLoc(item, ""));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void registerBlockRender(Block block)
/*     */   {
/* 150 */     Item itemBlockSimple = ItemBlock.func_150898_a(block);
/* 151 */     RenderItem ri = Minecraft.func_71410_x().func_175599_af();
/* 152 */     ri.func_175037_a().func_178086_a(itemBlockSimple, 0, itemModelResLoc(itemBlockSimple, ""));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ModelResourceLocation itemModelResLoc(Item item, String add)
/*     */   {
/* 159 */     return new ModelResourceLocation(item.getRegistryName() + add, "inventory");
/*     */   }
/*     */   
/*     */   public ModelResourceLocation blockModelResLoc(Block block, String add)
/*     */   {
/* 164 */     return new ModelResourceLocation(block.getRegistryName() + add, "inventory");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityPlayer getPlayerEntity(MessageContext ctx)
/*     */   {
/* 175 */     return ctx.side.isClient() ? Minecraft.func_71410_x().field_71439_g : super.getPlayerEntity(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */   public EntityPlayer getClientPlayer()
/*     */   {
/* 181 */     return Minecraft.func_71410_x().field_71439_g;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\ClientProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */