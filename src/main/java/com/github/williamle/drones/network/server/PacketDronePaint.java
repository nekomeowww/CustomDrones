/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.AbstractMap.SimpleEntry;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map.Entry;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*    */ import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.api.model.Color;
/*    */ import williamle.drones.drone.DroneAppearance.ColorPalette;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ public class PacketDronePaint implements IMessage
/*    */ {
/*    */   int dim;
/*    */   int droneID;
/* 23 */   List<Map.Entry<String, Color>> entries = new ArrayList();
/*    */   
/*    */ 
/*    */   public PacketDronePaint() {}
/*    */   
/*    */ 
/*    */   public PacketDronePaint(EntityDrone drone, String s, Color c)
/*    */   {
/* 31 */     this(drone, toList(s, c));
/*    */   }
/*    */   
/*    */   public PacketDronePaint(EntityDrone drone, List<Map.Entry<String, Color>> es)
/*    */   {
/* 36 */     this.dim = drone.field_70170_p.field_73011_w.getDimension();
/* 37 */     this.droneID = drone.getDroneID();
/* 38 */     this.entries = es;
/*    */   }
/*    */   
/*    */   public static List<Map.Entry<String, Color>> toList(String s, Color c)
/*    */   {
/* 43 */     List<Map.Entry<String, Color>> list = new ArrayList();
/* 44 */     list.add(new AbstractMap.SimpleEntry(s, c));
/* 45 */     return list;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 51 */     this.dim = buffer.readInt();
/* 52 */     this.droneID = buffer.readInt();
/* 53 */     int count = buffer.readInt();
/* 54 */     for (int a = 0; a < count; a++)
/*    */     {
/* 56 */       long l = buffer.readLong();
/* 57 */       Color color = l > 0L ? new Color(l) : null;
/* 58 */       String partName = ByteBufUtils.readUTF8String(buffer);
/* 59 */       this.entries.add(new AbstractMap.SimpleEntry(partName, color));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 66 */     buffer.writeInt(this.dim);
/* 67 */     buffer.writeInt(this.droneID);
/* 68 */     int count = this.entries.size();
/* 69 */     buffer.writeInt(count);
/* 70 */     for (int a = 0; a < count; a++)
/*    */     {
/* 72 */       String partName = (String)((Map.Entry)this.entries.get(a)).getKey();
/* 73 */       Color color = (Color)((Map.Entry)this.entries.get(a)).getValue();
/* 74 */       if (color != null) buffer.writeLong(color.toLong()); else
/* 75 */         buffer.writeLong(-1L);
/* 76 */       ByteBufUtils.writeUTF8String(buffer, partName);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketDronePaint>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketDronePaint message, MessageContext ctx)
/*    */     {
/* 85 */       World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
/* 86 */       if (world != null)
/*    */       {
/* 88 */         EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
/* 89 */         if (drone != null)
/*    */         {
/* 91 */           for (Map.Entry<String, Color> entry : message.entries)
/*    */           {
/* 93 */             drone.droneInfo.appearance.palette.setPaletteColor((String)entry.getKey(), (Color)entry.getValue());
/*    */           }
/* 95 */           drone.droneInfo.updateDroneInfoToClient(player);
/*    */         }
/*    */       }
/* 98 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketDronePaint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */