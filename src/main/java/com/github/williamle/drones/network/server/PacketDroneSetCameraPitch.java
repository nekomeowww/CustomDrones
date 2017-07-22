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
/*    */ public class PacketDroneSetCameraPitch
/*    */   implements IMessage
/*    */ {
/*    */   int droneID;
/*    */   float camera;
/*    */   
/*    */   public PacketDroneSetCameraPitch() {}
/*    */   
/*    */   public PacketDroneSetCameraPitch(EntityDrone drone, float b)
/*    */   {
/* 26 */     this.droneID = drone.getDroneID();
/* 27 */     this.camera = b;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 33 */     this.droneID = buffer.readInt();
/* 34 */     this.camera = buffer.readFloat();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 40 */     buffer.writeInt(this.droneID);
/* 41 */     buffer.writeFloat(this.camera);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneSetCameraPitch>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneSetCameraPitch message, MessageContext ctx)
/*    */     {
/* 49 */       if (player != null)
/*    */       {
/* 51 */         World world = player.field_70170_p;
/* 52 */         if (world != null)
/*    */         {
/* 54 */           EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 55 */           if (drone != null)
/*    */           {
/* 57 */             drone.setCameraPitch(message.camera);
/*    */           }
/*    */         }
/*    */       }
/* 61 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneSetCameraPitch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */