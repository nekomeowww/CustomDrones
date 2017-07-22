/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.drone.module.Module;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ import williamle.drones.item.ItemDroneModule;
/*    */ 
/*    */ public class PacketDroneUninstallMod implements IMessage
/*    */ {
/*    */   int droneID;
/*    */   Module mod;
/*    */   
/*    */   public PacketDroneUninstallMod() {}
/*    */   
/*    */   public PacketDroneUninstallMod(EntityDrone drone, Module m)
/*    */   {
/* 24 */     this.droneID = drone.getDroneID();
/* 25 */     this.mod = m;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 31 */     this.droneID = buffer.readInt();
/* 32 */     this.mod = Module.getModuleByID(ByteBufUtils.readUTF8String(buffer));
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 38 */     buffer.writeInt(this.droneID);
/* 39 */     ByteBufUtils.writeUTF8String(buffer, this.mod.getID());
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneUninstallMod>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneUninstallMod message, MessageContext ctx)
/*    */     {
/* 47 */       if (player != null)
/*    */       {
/* 49 */         World world = player.field_70170_p;
/* 50 */         if (world != null)
/*    */         {
/* 52 */           EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 53 */           if (drone != null)
/*    */           {
/* 55 */             if (drone.droneInfo.modsNBT != null)
/*    */             {
/* 57 */               drone.droneInfo.modsNBT.func_82580_o("MNBT" + message.mod.getID());
/*    */             }
/* 59 */             drone.droneInfo.disabledMods.remove(message.mod);
/* 60 */             drone.droneInfo.mods.remove(message.mod);
/* 61 */             drone.droneInfo.updateDroneInfoToClient(player);
/* 62 */             drone.func_70099_a(ItemDroneModule.itemModule(message.mod), 0.0F);
/*    */           }
/*    */         }
/*    */       }
/* 66 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneUninstallMod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */