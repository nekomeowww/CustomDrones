/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.drone.module.Module;
/*    */ import williamle.drones.drone.module.ModuleMine;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ public class PacketDroneSetMineLimits implements net.minecraftforge.fml.common.network.simpleimpl.IMessage
/*    */ {
/*    */   int dim;
/*    */   int droneID;
/*    */   boolean disable;
/*    */   int x0;
/*    */   int y0;
/*    */   int z0;
/*    */   int x1;
/*    */   int y1;
/*    */   int z1;
/*    */   
/*    */   public PacketDroneSetMineLimits() {}
/*    */   
/*    */   public PacketDroneSetMineLimits(EntityDrone drone, boolean dis, int a, int b, int c, int d, int e, int f)
/*    */   {
/* 26 */     this.dim = drone.field_70170_p.field_73011_w.getDimension();
/* 27 */     this.droneID = drone.getDroneID();
/* 28 */     this.disable = dis;
/* 29 */     this.x0 = a;
/* 30 */     this.y0 = b;
/* 31 */     this.z0 = c;
/* 32 */     this.x1 = d;
/* 33 */     this.y1 = e;
/* 34 */     this.z1 = f;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 40 */     this.dim = buffer.readInt();
/* 41 */     this.droneID = buffer.readInt();
/* 42 */     this.disable = buffer.readBoolean();
/* 43 */     this.x0 = buffer.readInt();
/* 44 */     this.y0 = buffer.readInt();
/* 45 */     this.z0 = buffer.readInt();
/* 46 */     this.x1 = buffer.readInt();
/* 47 */     this.y1 = buffer.readInt();
/* 48 */     this.z1 = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 54 */     buffer.writeInt(this.dim);
/* 55 */     buffer.writeInt(this.droneID);
/* 56 */     buffer.writeBoolean(this.disable);
/* 57 */     buffer.writeInt(this.x0);
/* 58 */     buffer.writeInt(this.y0);
/* 59 */     buffer.writeInt(this.z0);
/* 60 */     buffer.writeInt(this.x1);
/* 61 */     buffer.writeInt(this.y1);
/* 62 */     buffer.writeInt(this.z1);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneSetMineLimits>
/*    */   {
/*    */     public net.minecraftforge.fml.common.network.simpleimpl.IMessage handleServerMessage(net.minecraft.entity.player.EntityPlayer player, PacketDroneSetMineLimits message, net.minecraftforge.fml.common.network.simpleimpl.MessageContext ctx)
/*    */     {
/* 70 */       net.minecraft.world.World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
/* 71 */       if (world != null)
/*    */       {
/* 73 */         EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 74 */         if (drone != null)
/*    */         {
/* 76 */           Module m = drone.droneInfo.getModuleWithFunctionOf(Module.mine1);
/* 77 */           if (message.disable)
/*    */           {
/* 79 */             ((ModuleMine)m).removeLimits(drone.droneInfo);
/*    */           }
/*    */           else
/*    */           {
/* 83 */             ((ModuleMine)m).setLimits(drone.droneInfo, message.x0, message.y0, message.z0, message.x1, message.y1, message.z1);
/*    */           }
/*    */           
/* 86 */           drone.droneInfo.updateDroneInfoToClient(player);
/*    */         }
/*    */       }
/* 89 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneSetMineLimits.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */