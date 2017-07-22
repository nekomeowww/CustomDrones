/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.network.AbstractMessageHandler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractServerMessageHandler<T extends IMessage>
/*    */   extends AbstractMessageHandler<T>
/*    */ {
/*    */   public final IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx)
/*    */   {
/* 22 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\AbstractServerMessageHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */