/*     */ package williamle.drones.handlers;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.event.world.ExplosionEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
/*     */ import williamle.drones.AchievementPageDrone;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.block.BlockCrafter;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.drone.module.ModuleDe;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.item.ItemDroneBit;
/*     */ import williamle.drones.item.ItemDroneFlyer;
/*     */ import williamle.drones.item.ItemDronePainter;
/*     */ import williamle.drones.item.ItemDronePart;
/*     */ import williamle.drones.item.ItemDroneScrew;
/*     */ import williamle.drones.item.ItemDroneSpawn;
/*     */ import williamle.drones.item.ItemPlasmaGun;
/*     */ 
/*     */ public class EventHandler
/*     */ {
/*     */   @SubscribeEvent(priority=EventPriority.HIGHEST)
/*     */   public void explosion(ExplosionEvent evn)
/*     */   {
/*  42 */     Explosion ex = evn.getExplosion();
/*  43 */     World world = evn.getWorld();
/*  44 */     BlockPos bp = new BlockPos(ex.getPosition());
/*  45 */     AxisAlignedBB aabb = new AxisAlignedBB(bp).func_186662_g(ModuleDe.getMaxPossibleRange());
/*  46 */     List<EntityDrone> drones = world.func_72872_a(EntityDrone.class, aabb);
/*  47 */     for (EntityDrone d : drones)
/*     */     {
/*  49 */       if (d.hasEnabled(Module.deexplosion))
/*     */       {
/*  51 */         double range = ((ModuleDe)Module.deexplosion).getRange(d);
/*  52 */         if (d.func_174813_aQ().func_186662_g(range).func_72318_a(ex.getPosition()))
/*     */         {
/*  54 */           evn.setCanceled(true);
/*  55 */           d.droneInfo.reduceBattery(20.0D * d.droneInfo.getBatteryConsumptionRate(d));
/*  56 */           world.func_184133_a(null, bp, SoundEvents.field_187646_bt, SoundCategory.PLAYERS, 2.5F, 1.0F);
/*  57 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void itemPickup(PlayerEvent.ItemPickupEvent evn)
/*     */   {
/*  66 */     itemReceived(evn.player, evn.pickedUp.func_92059_d());
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void itemCraft(PlayerEvent.ItemCraftedEvent evn)
/*     */   {
/*  72 */     itemReceived(evn.player, evn.crafting);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void itemSmelt(PlayerEvent.ItemSmeltedEvent evn)
/*     */   {
/*  78 */     itemReceived(evn.player, evn.smelting);
/*     */   }
/*     */   
/*     */   public void itemReceived(EntityPlayer player, ItemStack is)
/*     */   {
/*  83 */     Item i = is.func_77973_b();
/*  84 */     if ((i instanceof ItemDroneBit))
/*     */     {
/*  86 */       player.func_71029_a(AchievementPageDrone.droneBit);
/*     */     }
/*  88 */     else if ((i instanceof ItemDronePart))
/*     */     {
/*  90 */       player.func_71029_a(AchievementPageDrone.dronePart);
/*     */     }
/*  92 */     else if ((i instanceof ItemDroneSpawn))
/*     */     {
/*  94 */       player.func_71029_a(AchievementPageDrone.droneSpawn);
/*  95 */       DroneInfo di = DronesMod.droneSpawn.getDroneInfo(is);
/*  96 */       if ((di.casing == 4) && (di.chip == 4) && (di.core == 4) && (di.engine == 4))
/*     */       {
/*  98 */         player.func_71029_a(AchievementPageDrone.droneSpawnBest);
/*     */       }
/*     */     }
/* 101 */     else if ((i instanceof ItemDroneFlyer))
/*     */     {
/* 103 */       player.func_71029_a(AchievementPageDrone.droneFlyer);
/*     */     }
/* 105 */     else if ((i instanceof ItemDronePainter))
/*     */     {
/* 107 */       player.func_71029_a(AchievementPageDrone.dronePaint);
/*     */     }
/* 109 */     else if ((i instanceof ItemDroneScrew))
/*     */     {
/* 111 */       player.func_71029_a(AchievementPageDrone.droneScrew);
/*     */     }
/* 113 */     else if ((i instanceof ItemPlasmaGun))
/*     */     {
/* 115 */       player.func_71029_a(AchievementPageDrone.dronePlasmaGun);
/*     */     }
/* 117 */     else if ((i instanceof ItemBlock))
/*     */     {
/* 119 */       Block b = ((ItemBlock)i).func_179223_d();
/* 120 */       if ((b instanceof BlockCrafter))
/*     */       {
/* 122 */         player.func_71029_a(AchievementPageDrone.droneCrafter);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\handlers\EventHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */