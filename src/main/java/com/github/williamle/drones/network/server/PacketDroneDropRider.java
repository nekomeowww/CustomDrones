/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketDroneDropRider
/*    */   implements IMessage
/*    */ {
/*    */   int droneID;
/*    */   
/*    */   public PacketDroneDropRider() {}
/*    */   
/*    */   public PacketDroneDropRider(EntityDrone drone)
/*    */   {
/* 25 */     this.droneID = drone.getDroneID();
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 31 */     this.droneID = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 37 */     buffer.writeInt(this.droneID);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneDropRider>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneDropRider message, MessageContext ctx)
/*    */     {
/* 45 */       if (player != null)
/*    */       {
/* 47 */         World world = player.field_70170_p;
/* 48 */         if (world != null)
/*    */         {
/* 50 */           EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 51 */           if (drone != null)
/*    */           {
/* 53 */             drone.dropRider();
/*    */           }
/*    */         }
/*    */       }
/* 57 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneDropRider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */