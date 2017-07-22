/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.DronesMod;
/*    */ import williamle.drones.api.path.Path;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ import williamle.drones.item.ItemDroneFlyer;
/*    */ import williamle.drones.network.PacketDispatcher;
/*    */ import williamle.drones.network.client.PacketDroneControllingPlayer;
/*    */ 
/*    */ public class PacketDroneControllerChange
/*    */   implements IMessage
/*    */ {
/*    */   int encoded;
/*    */   
/*    */   public PacketDroneControllerChange() {}
/*    */   
/*    */   public PacketDroneControllerChange(int i)
/*    */   {
/* 25 */     this.encoded = i;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 31 */     this.encoded = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 37 */     buffer.writeInt(this.encoded);
/*    */   }
/*    */   
/*    */ 
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneControllerChange>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneControllerChange message, MessageContext ctx)
/*    */     {
/* 46 */       boolean unbindDrone = false;
/* 47 */       EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(player.field_70170_p, player.func_184614_ca());
/* 48 */       if (message.encoded <= 0)
/*    */       {
/* 50 */         int newFreq = message.encoded * -1;
/* 51 */         int oldFreq = DronesMod.droneFlyer.getControllerFreq(player.func_184614_ca());
/* 52 */         if (drone != null)
/*    */         {
/* 54 */           drone.droneInfo.droneFreq = newFreq;
/* 55 */           drone.droneInfo.isChanged = true;
/*    */         }
/* 57 */         DronesMod.droneFlyer.setControllerFreq(player.func_184614_ca(), newFreq);
/*    */       }
/* 59 */       else if (message.encoded == 2)
/*    */       {
/* 61 */         DronesMod.droneFlyer.setNextFlyMode(player.func_184614_ca());
/*    */       }
/* 63 */       else if (message.encoded == 3)
/*    */       {
/* 65 */         unbindDrone = true;
/*    */       }
/* 67 */       else if (message.encoded == 4)
/*    */       {
/* 69 */         if (drone != null)
/*    */         {
/* 71 */           drone.setNextFlyingMode();
/* 72 */           PacketDispatcher.sendTo(new PacketDroneControllingPlayer(drone), (EntityPlayerMP)player);
/*    */         }
/*    */       }
/* 75 */       else if (message.encoded == 5)
/*    */       {
/* 77 */         if (drone != null)
/*    */         {
/* 79 */           drone.recordingPath = new Path();
/* 80 */           drone.automatedPath = null;
/*    */         }
/*    */       }
/* 83 */       if (unbindDrone)
/*    */       {
/* 85 */         if (drone != null)
/*    */         {
/* 87 */           drone.droneInfo.droneFreq = -1;
/* 88 */           drone.droneInfo.isChanged = true;
/*    */         }
/* 90 */         DronesMod.droneFlyer.setControllingDrone(player, player.func_184614_ca(), null);
/*    */       }
/* 92 */       if ((drone != null) && (drone.droneInfo.isChanged))
/*    */       {
/* 94 */         drone.droneInfo.updateDroneInfoToClient(player);
/*    */       }
/* 96 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneControllerChange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */