/*    */ package williamle.drones.network;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import williamle.drones.CommonProxy;
/*    */ import williamle.drones.DronesMod;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractMessageHandler<T extends IMessage>
/*    */   implements IMessageHandler<T, IMessage>
/*    */ {
/*    */   @SideOnly(Side.CLIENT)
/*    */   public abstract IMessage handleClientMessage(EntityPlayer paramEntityPlayer, T paramT, MessageContext paramMessageContext);
/*    */   
/*    */   public abstract IMessage handleServerMessage(EntityPlayer paramEntityPlayer, T paramT, MessageContext paramMessageContext);
/*    */   
/*    */   public IMessage onMessage(T message, MessageContext ctx)
/*    */   {
/* 64 */     if (ctx.side.isClient())
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/* 69 */       return handleClientMessage(DronesMod.proxy.getPlayerEntity(ctx), message, ctx);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 74 */     return handleServerMessage(DronesMod.proxy.getPlayerEntity(ctx), message, ctx);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\AbstractMessageHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */