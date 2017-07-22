/*    */ package williamle.drones.network.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.AbstractMap.SimpleEntry;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map.Entry;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.entity.player.PlayerCapabilities;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import williamle.drones.api.helpers.EntityHelper;
/*    */ 
/*    */ public class PacketCrafter
/*    */   implements IMessage
/*    */ {
/*    */   ItemStack output;
/*    */   List<Map.Entry<Byte, Byte>> reduceIndexes;
/*    */   
/*    */   public PacketCrafter() {}
/*    */   
/*    */   public PacketCrafter(ItemStack output, List<Map.Entry<Byte, Byte>> reduceIndexes)
/*    */   {
/* 29 */     this.output = output;
/* 30 */     this.reduceIndexes = reduceIndexes;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fromBytes(ByteBuf buffer)
/*    */   {
/* 36 */     this.reduceIndexes = new ArrayList();
/* 37 */     this.output = ByteBufUtils.readItemStack(buffer);
/* 38 */     this.output.field_77994_a = buffer.readInt();
/* 39 */     int indexesCount = buffer.readInt();
/* 40 */     for (int a = 0; a < indexesCount; a++)
/*    */     {
/* 42 */       Byte index = Byte.valueOf(buffer.readByte());
/* 43 */       Byte count = Byte.valueOf(buffer.readByte());
/* 44 */       this.reduceIndexes.add(new AbstractMap.SimpleEntry(index, count));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void toBytes(ByteBuf buffer)
/*    */   {
/* 51 */     int stackSize = this.output.field_77994_a;
/* 52 */     this.output.field_77994_a = 1;
/* 53 */     ByteBufUtils.writeItemStack(buffer, this.output);
/* 54 */     buffer.writeInt(stackSize);
/* 55 */     buffer.writeInt(this.reduceIndexes.size());
/* 56 */     for (int a = 0; a < this.reduceIndexes.size(); a++)
/*    */     {
/* 58 */       Map.Entry<Byte, Byte> entry = (Map.Entry)this.reduceIndexes.get(a);
/* 59 */       if (entry != null)
/*    */       {
/* 61 */         buffer.writeByte(((Byte)entry.getKey()).byteValue());
/* 62 */         buffer.writeByte(((Byte)entry.getValue()).byteValue());
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     extends AbstractServerMessageHandler<PacketCrafter>
/*    */   {
/*    */     public IMessage handleServerMessage(EntityPlayer player, PacketCrafter message, MessageContext ctx)
/*    */     {
/* 72 */       InventoryPlayer invp = player.field_71071_by;
/* 73 */       if (!player.field_71075_bZ.field_75098_d)
/*    */       {
/* 75 */         for (Map.Entry<Byte, Byte> entry : message.reduceIndexes)
/*    */         {
/* 77 */           invp.func_70298_a(((Byte)entry.getKey()).byteValue(), ((Byte)entry.getValue()).byteValue());
/*    */         }
/*    */       }
/* 80 */       ItemStack remain = EntityHelper.addItemStackToInv(invp, message.output);
/* 81 */       if (remain != null)
/*    */       {
/* 83 */         EntityItem ei = new EntityItem(player.field_70170_p, player.field_70165_t, player.field_70163_u, player.field_70161_v, remain);
/* 84 */         player.field_70170_p.func_72838_d(ei);
/*    */       }
/* 86 */       invp.func_70296_d();
/* 87 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\network\server\PacketCrafter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */