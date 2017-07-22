/*     */ package williamle.drones.handlers;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.MovementInputFromOptions;
/*     */ import net.minecraftforge.common.config.Configuration;
/*     */ import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import williamle.drones.ConfigControl;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.drone.module.ModuleCamera;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.item.ItemDroneFlyer;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ public class ClientEventHandler
/*     */ {
/*     */   public static MovementInputFromOptions playerMovementInput;
/*     */   
/*     */   @SubscribeEvent(priority=net.minecraftforge.fml.common.eventhandler.EventPriority.LOWEST)
/*     */   public void tick(TickEvent evn)
/*     */   {
/*  34 */     if (evn.type == TickEvent.Type.RENDER)
/*     */     {
/*  36 */       Minecraft mc = Minecraft.func_71410_x();
/*  37 */       Entity viewEntity = mc != null ? mc.func_175606_aa() : null;
/*  38 */       if ((evn.phase == TickEvent.Phase.START) && ((viewEntity instanceof EntityDrone)))
/*     */       {
/*  40 */         mc.func_147108_a(null);
/*  41 */         mc.field_71474_y.field_74319_N = true;
/*  42 */         mc.field_71474_y.field_74320_O = 0;
/*  43 */         if ((Keyboard.isCreated()) && (Keyboard.isKeyDown(1)))
/*     */         {
/*  45 */           ((EntityDrone)mc.func_175606_aa()).setCameraMode(false);
/*  46 */           mc.field_71474_y.field_74319_N = ModuleCamera.prevHideGui;
/*  47 */           mc.func_175607_a(ModuleCamera.prevRenderView);
/*     */         }
/*     */       }
/*     */     }
/*  51 */     if ((evn.type == TickEvent.Type.CLIENT) && (evn.phase == TickEvent.Phase.START))
/*     */     {
/*  53 */       Minecraft mc = Minecraft.func_71410_x();
/*  54 */       if ((mc != null) && (mc.field_71462_r == null))
/*     */       {
/*  56 */         EntityPlayer p = mc.field_71439_g;
/*  57 */         if ((p != null) && (p.func_184614_ca() != null) && 
/*  58 */           (DronesMod.droneFlyer.getFlyMode(p.func_184614_ca()) == 3))
/*     */         {
/*  60 */           EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(p.field_70170_p, p.func_184614_ca());
/*  61 */           if ((drone != null) && (drone.isControllerFlying()))
/*     */           {
/*  63 */             drone.setControllingPlayer(p);
/*     */           }
/*  65 */           int u = mc.field_71474_y.field_74351_w.func_151470_d() ? 1 : 0;
/*  66 */           int d = mc.field_71474_y.field_74368_y.func_151470_d() ? 1 : 0;
/*  67 */           int l = mc.field_71474_y.field_74370_x.func_151470_d() ? 1 : 0;
/*  68 */           int r = mc.field_71474_y.field_74366_z.func_151470_d() ? 1 : 0;
/*  69 */           int j = mc.field_71474_y.field_74314_A.func_151470_d() ? 1 : 0;
/*  70 */           int s = mc.field_71474_y.field_74311_E.func_151470_d() ? 1 : 0;
/*  71 */           if ((p.func_184218_aH()) && ((p.func_184187_bx() instanceof EntityDrone)))
/*     */           {
/*  73 */             j = mc.field_71474_y.field_74351_w.func_151470_d() ? 1 : 0;
/*  74 */             s = mc.field_71474_y.field_74368_y.func_151470_d() ? 1 : 0;
/*  75 */             u = mc.field_71474_y.field_74314_A.func_151470_d() ? 1 : 0;
/*  76 */             d = mc.field_71474_y.field_74311_E.func_151470_d() ? 1 : 0;
/*     */           }
/*  78 */           int buttonCombination = u | d << 1 | l << 2 | r << 3 | j << 4 | s << 5;
/*  79 */           if (buttonCombination > 0)
/*     */           {
/*  81 */             PacketDispatcher.sendToServer(new williamle.drones.network.server.PacketDroneButtonControl(buttonCombination));
/*  82 */             if ((p instanceof EntityPlayerSP))
/*     */             {
/*  84 */               if ((((EntityPlayerSP)p).field_71158_b instanceof MovementInputFromOptions))
/*     */               {
/*  86 */                 playerMovementInput = (MovementInputFromOptions)((EntityPlayerSP)p).field_71158_b;
/*     */               }
/*  88 */               ((EntityPlayerSP)p).field_71158_b = new net.minecraft.util.MovementInput();
/*     */             }
/*     */           }
/*     */         }
/*  92 */         else if ((p instanceof EntityPlayerSP))
/*     */         {
/*  94 */           if (!(((EntityPlayerSP)p).field_71158_b instanceof MovementInputFromOptions))
/*     */           {
/*  96 */             ((EntityPlayerSP)p).field_71158_b = playerMovementInput;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event)
/*     */   {
/* 106 */     if ((event.getModID() == "drones") && (event.getConfigID() == ConfigControl.CONFIGID))
/*     */     {
/* 108 */       DronesMod.configControl.syncConfig();
/* 109 */       DronesMod.configControl.config.save();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\handlers\ClientEventHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */