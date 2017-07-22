/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.WorldProvider;
/*    */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*    */ import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.drone.module.Module;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketDroneSwitchMod
/*    */   implements IMessage
/*    */ {
/*    */   int dim;
/*    */   int droneID;
/*    */   Module mod;
/*    */   boolean state;
/*    */   
/*    */   public PacketDroneSwitchMod() {}
/*    */   
/*    */   public PacketDroneSwitchMod(EntityDrone drone, Module m, boolean d)
/*    */   {
/* 31 */     this.dim = drone.field_70170_p.field_73011_w.getDimension();
/* 32 */     this.droneID = drone.getDroneID();
/* 33 */     this.mod = m;
/* 34 */     this.state = d;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 40 */     this.dim = buffer.readInt();
/* 41 */     this.droneID = buffer.readInt();
/* 42 */     this.mod = Module.getModuleByID(ByteBufUtils.readUTF8String(buffer));
/* 43 */     this.state = buffer.readBoolean();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 49 */     buffer.writeInt(this.dim);
/* 50 */     buffer.writeInt(this.droneID);
/* 51 */     ByteBufUtils.writeUTF8String(buffer, this.mod.getID());
/* 52 */     buffer.writeBoolean(this.state);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneSwitchMod>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneSwitchMod message, MessageContext ctx)
/*    */     {
/* 60 */       World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
/* 61 */       if (world != null)
/*    */       {
/* 63 */         EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 64 */         if (drone != null)
/*    */         {
/* 66 */           drone.droneInfo.switchModule(drone, message.mod, message.state);
/* 67 */           drone.droneInfo.updateDroneInfoToClient(player);
/*    */         }
/*    */       }
/* 70 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneSwitchMod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */