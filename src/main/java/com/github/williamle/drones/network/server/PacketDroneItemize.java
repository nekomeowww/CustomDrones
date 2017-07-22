/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.DronesMod;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ import williamle.drones.item.ItemDroneSpawn;
/*    */ 
/*    */ public class PacketDroneItemize
/*    */   implements IMessage
/*    */ {
/*    */   int droneID;
/*    */   
/*    */   public PacketDroneItemize() {}
/*    */   
/*    */   public PacketDroneItemize(EntityDrone drone)
/*    */   {
/* 24 */     this.droneID = drone.getDroneID();
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 30 */     this.droneID = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 36 */     buffer.writeInt(this.droneID);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneItemize>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneItemize message, MessageContext ctx)
/*    */     {
/* 44 */       if (player != null)
/*    */       {
/* 46 */         World world = player.field_70170_p;
/* 47 */         if (world != null)
/*    */         {
/* 49 */           EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 50 */           if (drone != null)
/*    */           {
/* 52 */             DroneInfo di = drone.droneInfo;
/* 53 */             ItemStack is = new ItemStack(DronesMod.droneSpawn);
/* 54 */             DronesMod.droneSpawn.setDroneInfo(is, di);
/* 55 */             drone.func_70106_y();
/* 56 */             EntityItem ei = new EntityItem(drone.field_70170_p, drone.field_70165_t, drone.field_70163_u, drone.field_70161_v, is);
/* 57 */             drone.field_70170_p.func_72838_d(ei);
/*    */           }
/*    */         }
/*    */       }
/* 61 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneItemize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */