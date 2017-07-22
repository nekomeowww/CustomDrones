/*    */ package williamle.drones.gui;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.IGuiHandler;
/*    */ import williamle.drones.api.gui.ContainerNothing;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ public class GuiHandler
/*    */   implements IGuiHandler
/*    */ {
/*    */   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
/*    */   {
/* 15 */     if (ID != 0)
/*    */     {
/*    */ 
/* 18 */       if (ID == 1)
/*    */       {
/*    */ 
/* 21 */         EntityDrone d = EntityDrone.getDroneFromID(world, x);
/* 22 */         if (d != null)
/*    */         {
/* 24 */           d.droneInfo.updateDroneInfoToClient(player);
/* 25 */           return new ContainerDrone(player.field_71071_by, d.droneInfo.inventory);
/*    */         }
/*    */       }
/* 28 */       else if (ID == 2)
/*    */       {
/*    */ 
/* 31 */         EntityDrone d = EntityDrone.getDroneFromID(world, x);
/* 32 */         if (d != null)
/*    */         {
/* 34 */           return new ContainerDroneStatus(d.droneInfo.inventory);
/*    */         }
/*    */       } else {
/* 37 */         if (ID == 3)
/*    */         {
/* 39 */           return new ContainerCrafter(player.field_71071_by);
/*    */         }
/* 41 */         if (ID == 4)
/*    */         {
/* 43 */           return new ContainerNothing();
/*    */         }
/* 45 */         if (ID == 5)
/*    */         {
/* 47 */           return new ContainerNothing(); }
/*    */       } }
/* 49 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
/*    */   {
/* 55 */     if (ID == 0)
/*    */     {
/* 57 */       return new GuiDroneFlyer(world, player);
/*    */     }
/* 59 */     if (ID == 1)
/*    */     {
/*    */ 
/* 62 */       EntityDrone d = EntityDrone.getDroneFromID(world, x);
/* 63 */       if (d != null) return new GuiDrone(player, d);
/*    */     }
/* 65 */     else if (ID == 2)
/*    */     {
/*    */ 
/* 68 */       EntityDrone d = EntityDrone.getDroneFromID(world, x);
/* 69 */       if (d != null) return new GuiDroneStatus(player, d);
/*    */     } else {
/* 71 */       if (ID == 3)
/*    */       {
/* 73 */         return new GuiCrafter(player);
/*    */       }
/* 75 */       if (ID == 4)
/*    */       {
/* 77 */         return new GuiPainter(EntityDrone.getDroneFromID(world, x));
/*    */       }
/* 79 */       if (ID == 5)
/*    */       {
/* 81 */         return new GuiScrew(EntityDrone.getDroneFromID(world, x)); }
/*    */     }
/* 83 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\GuiHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */