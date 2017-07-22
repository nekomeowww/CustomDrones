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
/*    */ public class PacketDroneModuleNBT
/*    */   implements IMessage
/*    */ {
/*    */   public int id;
/*    */   public NBTTagCompound tag;
/*    */   
/*    */   public PacketDroneModuleNBT() {}
/*    */   
/*    */   public PacketDroneModuleNBT(DroneInfo di)
/*    */   {
/* 23 */     this.id = di.id;
/* 24 */     this.tag = di.modsNBT;
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 30 */     buffer.writeInt(this.id);
/* 31 */     ByteBufUtils.writeTag(buffer, this.tag);
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 37 */     this.id = buffer.readInt();
/* 38 */     this.tag = ByteBufUtils.readTag(buffer);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractClientMessageHandler<PacketDroneModuleNBT>
/*    */   {
/*    */     public IMessage handleClientMessage(EntityPlayer player, PacketDroneModuleNBT message, MessageContext ctx)
/*    */     {
/* 46 */       EntityDrone drone = EntityDrone.getDroneFromID(player.field_70170_p, message.id);
/* 47 */       if (drone != null)
/*    */       {
/* 49 */         drone.droneInfo.readModulesNBT(message.tag);
/*    */       }
/* 51 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\client\PacketDroneModuleNBT.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */