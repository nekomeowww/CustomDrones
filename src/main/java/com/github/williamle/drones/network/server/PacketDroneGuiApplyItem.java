/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.DronesMod;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ import williamle.drones.gui.ContainerDrone;
/*    */ import williamle.drones.item.ItemDroneModule;
/*    */ 
/*    */ public class PacketDroneGuiApplyItem implements IMessage
/*    */ {
/*    */   int dim;
/*    */   int droneID;
/*    */   
/*    */   public PacketDroneGuiApplyItem() {}
/*    */   
/*    */   public PacketDroneGuiApplyItem(EntityDrone drone)
/*    */   {
/* 27 */     this.dim = drone.field_70170_p.field_73011_w.getDimension();
/* 28 */     this.droneID = drone.getDroneID();
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 34 */     this.dim = buffer.readInt();
/* 35 */     this.droneID = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 41 */     buffer.writeInt(this.dim);
/* 42 */     buffer.writeInt(this.droneID);
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneGuiApplyItem>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneGuiApplyItem message, MessageContext ctx)
/*    */     {
/* 50 */       World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
/* 51 */       if (world != null)
/*    */       {
/* 53 */         EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 54 */         if (drone != null)
/*    */         {
/* 56 */           ContainerDrone c = (ContainerDrone)((player.field_71070_bA instanceof ContainerDrone) ? player.field_71070_bA : null);
/*    */           
/* 58 */           Slot itemApply = c.func_75147_a(c.module, 0);
/* 59 */           ItemStack is = itemApply.func_75211_c();
/* 60 */           if (is != null)
/*    */           {
/* 62 */             Item i = is.func_77973_b();
/* 63 */             if (i == DronesMod.droneModule)
/*    */             {
/* 65 */               drone.droneInfo.applyModule(ItemDroneModule.getModule(is));
/* 66 */               is.field_77994_a -= 1;
/* 67 */               if (is.field_77994_a <= 0) is = null;
/*    */             }
/*    */             else
/*    */             {
/* 71 */               is = drone.droneInfo.applyItem(drone, is);
/*    */             }
/*    */           }
/* 74 */           drone.droneInfo.updateDroneInfoToClient(player);
/* 75 */           itemApply.func_75215_d(is);
/*    */         }
/*    */       }
/* 78 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneGuiApplyItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */