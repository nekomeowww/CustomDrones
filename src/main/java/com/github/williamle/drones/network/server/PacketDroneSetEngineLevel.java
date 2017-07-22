/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.WorldProvider;
/*    */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ 
/*    */ public class PacketDroneSetEngineLevel
/*    */   implements IMessage
/*    */ {
/*    */   int dim;
/*    */   int droneID;
/*    */   double engineLevel;
/*    */   
/*    */   public PacketDroneSetEngineLevel() {}
/*    */   
/*    */   public PacketDroneSetEngineLevel(EntityDrone drone, double el)
/*    */   {
/* 26 */     this.dim = drone.field_70170_p.field_73011_w.getDimension();
/* 27 */     this.droneID = drone.getDroneID();
/* 28 */     this.engineLevel = el;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 34 */     this.dim = buffer.readInt();
/* 35 */     this.droneID = buffer.readInt();
/* 36 */     this.engineLevel = buffer.readDouble();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 42 */     buffer.writeInt(this.dim);
/* 43 */     buffer.writeInt(this.droneID);
/* 44 */     buffer.writeDouble(this.engineLevel);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneSetEngineLevel>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneSetEngineLevel message, MessageContext ctx)
/*    */     {
/* 52 */       World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
/* 53 */       if (world != null)
/*    */       {
/* 55 */         EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 56 */         if (drone != null)
/*    */         {
/* 58 */           drone.droneInfo.setEngineLevel(message.engineLevel);
/* 59 */           drone.droneInfo.updateDroneInfoToClient(player);
/*    */         }
/*    */       }
/* 62 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneSetEngineLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */