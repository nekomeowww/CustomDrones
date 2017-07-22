/*     */ package williamle.drones.network;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraftforge.fml.common.network.NetworkRegistry;
/*     */ import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import williamle.drones.network.client.AbstractClientMessageHandler;
/*     */ import williamle.drones.network.client.PacketDroneControllingPlayer;
/*     */ import williamle.drones.network.client.PacketDroneControllingPlayer.Handler;
/*     */ import williamle.drones.network.client.PacketDroneInfo;
/*     */ import williamle.drones.network.client.PacketDroneInfo.Handler;
/*     */ import williamle.drones.network.client.PacketDroneModuleNBT;
/*     */ import williamle.drones.network.client.PacketDroneModuleNBT.Handler;
/*     */ import williamle.drones.network.server.PacketCrafter;
/*     */ import williamle.drones.network.server.PacketCrafter.Handler;
/*     */ import williamle.drones.network.server.PacketDroneButtonControl;
/*     */ import williamle.drones.network.server.PacketDroneButtonControl.Handler;
/*     */ import williamle.drones.network.server.PacketDroneControllerChange;
/*     */ import williamle.drones.network.server.PacketDroneControllerChange.Handler;
/*     */ import williamle.drones.network.server.PacketDroneDropRider;
/*     */ import williamle.drones.network.server.PacketDroneDropRider.Handler;
/*     */ import williamle.drones.network.server.PacketDroneGuiApplyItem;
/*     */ import williamle.drones.network.server.PacketDroneGuiApplyItem.Handler;
/*     */ import williamle.drones.network.server.PacketDroneItemize;
/*     */ import williamle.drones.network.server.PacketDroneItemize.Handler;
/*     */ import williamle.drones.network.server.PacketDronePaint;
/*     */ import williamle.drones.network.server.PacketDronePaint.Handler;
/*     */ import williamle.drones.network.server.PacketDroneRename;
/*     */ import williamle.drones.network.server.PacketDroneRename.Handler;
/*     */ import williamle.drones.network.server.PacketDroneRequireUpdate;
/*     */ import williamle.drones.network.server.PacketDroneRequireUpdate.Handler;
/*     */ import williamle.drones.network.server.PacketDroneScrew;
/*     */ import williamle.drones.network.server.PacketDroneScrew.Handler;
/*     */ import williamle.drones.network.server.PacketDroneSetCameraMode;
/*     */ import williamle.drones.network.server.PacketDroneSetCameraMode.Handler;
/*     */ import williamle.drones.network.server.PacketDroneSetCameraPitch;
/*     */ import williamle.drones.network.server.PacketDroneSetCameraPitch.Handler;
/*     */ import williamle.drones.network.server.PacketDroneSetEngineLevel;
/*     */ import williamle.drones.network.server.PacketDroneSetEngineLevel.Handler;
/*     */ import williamle.drones.network.server.PacketDroneSetMineLimits;
/*     */ import williamle.drones.network.server.PacketDroneSetMineLimits.Handler;
/*     */ import williamle.drones.network.server.PacketDroneSetReturnPos;
/*     */ import williamle.drones.network.server.PacketDroneSetReturnPos.Handler;
/*     */ import williamle.drones.network.server.PacketDroneSwitchMod;
/*     */ import williamle.drones.network.server.PacketDroneSwitchMod.Handler;
/*     */ import williamle.drones.network.server.PacketDroneTransferXP;
/*     */ import williamle.drones.network.server.PacketDroneTransferXP.Handler;
/*     */ import williamle.drones.network.server.PacketDroneUninstallMod;
/*     */ import williamle.drones.network.server.PacketDroneUninstallMod.Handler;
/*     */ 
/*     */ public class PacketDispatcher
/*     */ {
/*  59 */   private static byte packetId = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel("drones");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void registerPackets()
/*     */   {
/*  74 */     registerMessage(PacketDroneButtonControl.Handler.class, PacketDroneButtonControl.class);
/*  75 */     registerMessage(PacketDroneInfo.Handler.class, PacketDroneInfo.class);
/*  76 */     registerMessage(PacketDroneControllerChange.Handler.class, PacketDroneControllerChange.class);
/*  77 */     registerMessage(PacketDroneRename.Handler.class, PacketDroneRename.class);
/*  78 */     registerMessage(PacketDroneSetEngineLevel.Handler.class, PacketDroneSetEngineLevel.class);
/*  79 */     registerMessage(PacketDroneGuiApplyItem.Handler.class, PacketDroneGuiApplyItem.class);
/*  80 */     registerMessage(PacketDroneSwitchMod.Handler.class, PacketDroneSwitchMod.class);
/*  81 */     registerMessage(PacketDroneTransferXP.Handler.class, PacketDroneTransferXP.class);
/*  82 */     registerMessage(PacketDroneRequireUpdate.Handler.class, PacketDroneRequireUpdate.class);
/*  83 */     registerMessage(PacketDroneDropRider.Handler.class, PacketDroneDropRider.class);
/*  84 */     registerMessage(PacketDroneControllingPlayer.Handler.class, PacketDroneControllingPlayer.class);
/*  85 */     registerMessage(PacketDroneSetReturnPos.Handler.class, PacketDroneSetReturnPos.class);
/*  86 */     registerMessage(PacketCrafter.Handler.class, PacketCrafter.class);
/*  87 */     registerMessage(PacketDroneSetCameraMode.Handler.class, PacketDroneSetCameraMode.class);
/*  88 */     registerMessage(PacketDroneItemize.Handler.class, PacketDroneItemize.class);
/*  89 */     registerMessage(PacketDroneUninstallMod.Handler.class, PacketDroneUninstallMod.class);
/*  90 */     registerMessage(PacketDroneSetMineLimits.Handler.class, PacketDroneSetMineLimits.class);
/*  91 */     registerMessage(PacketDroneSetCameraPitch.Handler.class, PacketDroneSetCameraPitch.class);
/*  92 */     registerMessage(PacketDroneModuleNBT.Handler.class, PacketDroneModuleNBT.class);
/*  93 */     registerMessage(PacketDronePaint.Handler.class, PacketDronePaint.class);
/*  94 */     registerMessage(PacketDroneScrew.Handler.class, PacketDroneScrew.class);
/*     */   }
/*     */   
/*     */ 
/*     */   private static final <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handlerClass, Class<REQ> messageClass)
/*     */   {
/* 100 */     Side side = AbstractClientMessageHandler.class.isAssignableFrom(handlerClass) ? Side.CLIENT : Side.SERVER;
/* 101 */     dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final void registerMessage(Class handlerClass, Class messageClass, Side side)
/*     */   {
/* 109 */     dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void sendTo(IMessage message, EntityPlayerMP player)
/*     */   {
/* 124 */     dispatcher.sendTo(message, player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point)
/*     */   {
/* 133 */     dispatcher.sendToAllAround(message, point);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range)
/*     */   {
/* 144 */     sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void sendToAllAround(IMessage message, EntityPlayer player, double range)
/*     */   {
/* 155 */     sendToAllAround(message, player.field_70170_p.field_73011_w.getDimension(), player.field_70165_t, player.field_70163_u, player.field_70161_v, range);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void sendToDimension(IMessage message, int dimensionId)
/*     */   {
/* 166 */     dispatcher.sendToDimension(message, dimensionId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void sendToServer(IMessage message)
/*     */   {
/* 175 */     dispatcher.sendToServer(message);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\PacketDispatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */