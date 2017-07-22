/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.drone.module.Module;
/*    */ import williamle.drones.drone.module.ModuleReturn;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ public class PacketDroneSetReturnPos
/*    */   implements IMessage
/*    */ {
/*    */   int droneID;
/*    */   
/*    */   public PacketDroneSetReturnPos() {}
/*    */   
/*    */   public PacketDroneSetReturnPos(EntityDrone drone)
/*    */   {
/* 22 */     this.droneID = drone.getDroneID();
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 28 */     this.droneID = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 34 */     buffer.writeInt(this.droneID);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneSetReturnPos>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneSetReturnPos message, MessageContext ctx)
/*    */     {
/* 42 */       World world = player.field_70170_p;
/* 43 */       if (world != null)
/*    */       {
/* 45 */         EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 46 */         if (drone != null)
/*    */         {
/* 48 */           ((ModuleReturn)drone.droneInfo.getModuleWithFunctionOf(Module.autoReturn)).setReturnPos(drone, drone
/* 49 */             .func_174791_d());
/* 50 */           drone.droneInfo.updateDroneInfoToClient(player);
/*    */         }
/*    */       }
/* 53 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneSetReturnPos.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */