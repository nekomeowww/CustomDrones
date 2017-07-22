/*    */ package williamle.drones.network.client;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketDroneControllingPlayer
/*    */   implements IMessage
/*    */ {
/*    */   int droneID;
/*    */   String player;
/*    */   
/*    */   public PacketDroneControllingPlayer() {}
/*    */   
/*    */   public PacketDroneControllingPlayer(EntityDrone drone)
/*    */   {
/* 24 */     this.droneID = drone.getDroneID();
/* 25 */     this.player = (drone.getControllingPlayer() == null ? "" : drone.getControllingPlayer().func_70005_c_());
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 31 */     this.droneID = buffer.readInt();
/* 32 */     this.player = ByteBufUtils.readUTF8String(buffer);
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 38 */     buffer.writeInt(this.droneID);
/* 39 */     ByteBufUtils.writeUTF8String(buffer, this.player);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractClientMessageHandler<PacketDroneControllingPlayer>
/*    */   {
/*    */     public IMessage handleClientMessage(EntityPlayer player, PacketDroneControllingPlayer message, MessageContext ctx)
/*    */     {
/* 47 */       if (player != null)
/*    */       {
/* 49 */         World world = player.field_70170_p;
/* 50 */         if (world != null)
/*    */         {
/* 52 */           EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 53 */           EntityPlayer eplayer = world.func_72924_a(message.player);
/* 54 */           if (drone != null)
/*    */           {
/* 56 */             drone.setControllingPlayer(eplayer);
/*    */           }
/*    */         }
/*    */       }
/* 60 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\client\PacketDroneControllingPlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */