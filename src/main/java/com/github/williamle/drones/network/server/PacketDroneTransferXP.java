/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.WorldProvider;
/*    */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.drone.module.Module;
/*    */ import williamle.drones.drone.module.ModuleCollect;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ public class PacketDroneTransferXP implements IMessage
/*    */ {
/*    */   int dim;
/*    */   int droneID;
/*    */   
/*    */   public PacketDroneTransferXP() {}
/*    */   
/*    */   public PacketDroneTransferXP(EntityDrone drone)
/*    */   {
/* 24 */     this.dim = drone.field_70170_p.field_73011_w.getDimension();
/* 25 */     this.droneID = drone.getDroneID();
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 31 */     this.dim = buffer.readInt();
/* 32 */     this.droneID = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 38 */     buffer.writeInt(this.dim);
/* 39 */     buffer.writeInt(this.droneID);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneTransferXP>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneTransferXP message, MessageContext ctx)
/*    */     {
/* 47 */       World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
/* 48 */       if (world != null)
/*    */       {
/* 50 */         EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 51 */         if (drone != null)
/*    */         {
/* 53 */           player.func_71023_q(((ModuleCollect)drone.droneInfo.getModuleWithFunctionOf(Module.xpCollect))
/* 54 */             .getCollectedXP(drone));
/* 55 */           ((ModuleCollect)drone.droneInfo.getModuleWithFunctionOf(Module.xpCollect)).setCollectedXP(drone, 0);
/*    */           
/* 57 */           drone.droneInfo.updateDroneInfoToClient(player);
/*    */         }
/*    */       }
/* 60 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneTransferXP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */