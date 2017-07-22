/*    */ package williamle.drones.network.client;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ 
/*    */ public class PacketDroneInfo
/*    */   implements IMessage
/*    */ {
/*    */   public DroneInfo droneInfo;
/*    */   
/*    */   public PacketDroneInfo() {}
/*    */   
/*    */   public PacketDroneInfo(DroneInfo di)
/*    */   {
/* 22 */     this.droneInfo = di;
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 28 */     NBTTagCompound di = new NBTTagCompound();
/* 29 */     this.droneInfo.writeToNBT(di);
/* 30 */     ByteBufUtils.writeTag(buffer, di);
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 36 */     NBTTagCompound di = ByteBufUtils.readTag(buffer);
/* 37 */     this.droneInfo = DroneInfo.fromNBT(di);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractClientMessageHandler<PacketDroneInfo>
/*    */   {
/*    */     public IMessage handleClientMessage(EntityPlayer player, PacketDroneInfo message, MessageContext ctx)
/*    */     {
/* 45 */       EntityDrone drone = EntityDrone.getDroneFromID(player.field_70170_p, message.droneInfo.id);
/* 46 */       if (drone != null) drone.setDroneInfo(message.droneInfo);
/* 47 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\client\PacketDroneInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */