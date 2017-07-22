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
/*    */ public class PacketDroneScrew
/*    */   implements IMessage
/*    */ {
/*    */   int dim;
/*    */   int droneID;
/*    */   int id;
/*    */   
/*    */   public PacketDroneScrew() {}
/*    */   
/*    */   public PacketDroneScrew(EntityDrone drone, int i)
/*    */   {
/* 25 */     this.dim = drone.field_70170_p.field_73011_w.getDimension();
/* 26 */     this.droneID = drone.getDroneID();
/* 27 */     this.id = i;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 33 */     this.dim = buffer.readInt();
/* 34 */     this.droneID = buffer.readInt();
/* 35 */     this.id = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 41 */     buffer.writeInt(this.dim);
/* 42 */     buffer.writeInt(this.droneID);
/* 43 */     buffer.writeInt(this.id);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneScrew>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneScrew message, MessageContext ctx)
/*    */     {
/* 51 */       World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
/* 52 */       if (world != null)
/*    */       {
/* 54 */         EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 55 */         if (drone != null)
/*    */         {
/* 57 */           drone.droneInfo.appearance.modelID = message.id;
/* 58 */           drone.droneInfo.updateDroneInfoToClient(player);
/*    */         }
/*    */       }
/* 61 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneScrew.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */