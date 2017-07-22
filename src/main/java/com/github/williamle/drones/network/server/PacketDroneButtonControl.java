/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.DronesMod;
/*    */ import williamle.drones.item.ItemDroneFlyer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketDroneButtonControl
/*    */   implements IMessage
/*    */ {
/*    */   public int buttonCombination;
/*    */   public ItemStack controller;
/*    */   
/*    */   public PacketDroneButtonControl() {}
/*    */   
/*    */   public PacketDroneButtonControl(int i)
/*    */   {
/* 23 */     this.buttonCombination = i;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 29 */     this.buttonCombination = buffer.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 37 */     buffer.writeInt(this.buttonCombination);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDroneButtonControl>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDroneButtonControl message, MessageContext ctx)
/*    */     {
/* 49 */       if (DronesMod.droneFlyer.getFlyMode(player.func_184614_ca()) == 3)
/*    */       {
/* 51 */         DronesMod.droneFlyer.flyDroneWithButton(player, message.buttonCombination);
/*    */       }
/* 53 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDroneButtonControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */