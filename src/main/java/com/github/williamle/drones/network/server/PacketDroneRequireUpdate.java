/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ import williamle.drones.network.PacketDispatcher;
/*    */ import williamle.drones.network.client.PacketDroneControllingPlayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketDroneRequireUpdate
/*    */   implements IMessage
/*    */ {
/*    */   int droneID;
/*    */   
/*    */   public PacketDroneRequireUpdate() {}
/*    */   
/*    */   public PacketDroneRequireUpdate(EntityDrone drone)
/*    */   {
/* 28 */     this.droneID = drone.getDroneID();
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 34 */     this.droneID = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 40 */     buffer.writeInt(this.droneID);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneRequireUpdate>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneRequireUpdate message, MessageContext ctx)
/*    */     {
/* 48 */       World world = player.field_70170_p;
/* 49 */       if (world != null)
/*    */       {
/* 51 */         EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 52 */         if (drone != null)
/*    */         {
/* 54 */           drone.droneInfo.updateDroneInfoToClient(player);
/* 55 */           PacketDispatcher.sendTo(new PacketDroneControllingPlayer(drone), (EntityPlayerMP)player);
/*    */         }
/*    */       }
/* 58 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneRequireUpdate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */